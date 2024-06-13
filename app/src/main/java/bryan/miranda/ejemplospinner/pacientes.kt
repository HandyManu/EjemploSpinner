package bryan.miranda.ejemplospinner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.dataClassDoctores

class pacientes : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_pacientes, container, false)

        //Mando a llamar a los elemntos
        val spDoctores = root.findViewById<Spinner>(R.id.spDoctores)
        //funcion para hacer el select de los nombres que voy a mostrar en el Spinner

        fun obtenerDoctores():List<dataClassDoctores>{
            //crearun objeto de la clase conexion
            val objConexion=ClaseConexion().cadenaConexion()

            //creo un statement que me ejecutara el select

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbDoctores")!!
            val ListadoDoctores = mutableListOf<dataClassDoctores>()

            while (resultSet.next()){
                val uuid = resultSet.getString("DoctorUUID")
                val nombre = resultSet.getString("nombreDoctor")
                val especialidad = resultSet.getString("especialidad")
                val telefono = resultSet.getString("telefono")
                val unDoctorCompleto = dataClassDoctores(uuid,nombre,especialidad,telefono)
                ListadoDoctores.add(unDoctorCompleto)
            }
            return  ListadoDoctores
        }
        //programamos el spiner para que muestre los datos del select

      CoroutineScope(Dispatchers.IO).launch {
          //obtengo los datos

          val ListadoDoctores = obtenerDoctores()
          val nombreDoctores = ListadoDoctores.map { it.nombreDoctor }
          withContext(Dispatchers.Main){
              //cear y modificar el adaptador
              val miAdaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,nombreDoctores)
              spDoctores.adapter=miAdaptador
          }

      }
        return root
    }
}