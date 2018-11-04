package com.arz.chech.collegegestion.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arz.chech.collegegestion.Publicacion;
import com.arz.chech.collegegestion.R;
import com.arz.chech.collegegestion.adapters.AdaptadorPublicaciones;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicacionesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ArrayList<Publicacion> listaPublicaciones;
    RecyclerView recyclerViewPublicaciones;

    public PublicacionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicacionesFragment newInstance(String param1, String param2) {
        PublicacionesFragment fragment = new PublicacionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_publicaciones, container, false);
        listaPublicaciones = new ArrayList<>();
        recyclerViewPublicaciones=vista.findViewById(R.id.Recyclerid);
        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarPublicaciones();
        AdaptadorPublicaciones adapter = new AdaptadorPublicaciones(listaPublicaciones);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"mensaje: "+listaPublicaciones.get(recyclerViewPublicaciones.getChildAdapterPosition(view)).getDetalle(),Toast.LENGTH_LONG).show();
                FragmentManager fm = getFragmentManager();
                DetalleFragment dialogFragment = new DetalleFragment ();
                Bundle b = new Bundle();
                b.putString("descripcion",listaPublicaciones.get(recyclerViewPublicaciones.getChildAdapterPosition(view)).getDescripcion()+"\n");
                b.putString ("detalle", listaPublicaciones.get(recyclerViewPublicaciones.getChildAdapterPosition(view)).getDetalle());
                dialogFragment.setArguments(b);
                dialogFragment.show(fm, "Sample Fragment");
            }
        });
        recyclerViewPublicaciones.setAdapter(adapter);


        return vista;
    }

    private void llenarPublicaciones(){
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias","ES RESPONSABILIDAD DE LA FAMILIA Y LA ESCUELA FORMAR CIUDADANOS RESPONSABLES Y\n" +
                "PUNTUALES, NUESTRO PAIS NECESITA ESTAR POBLADO DE SERES HUMANOS CAPACES DE\n" +
                "TRANSFORAMAR SU ENTORNO CON VALORES CIVILES.\n" +
                "HORARIO DE ENTRADA SECUNDARIA 7.00AM SALIDA 14.30 PM\n" +
                "HORARIO DE ENTRADA PRIMARIA 7:30 AM SALIDA 14:00 PM\n" +
                "HOARARIO DE ENTRADA DE PREESCOLAR: 8.30 AM SALIDA 13:00 PM.\n" +
                "EL TIEMPO MAXIMO DE TOLERANCIA ES 10 MINUTOS, DESPUES DE ESE HORARIO TRES RETARDOS\n" +
                "AL MES CAUSA SUSPENSIÓN.\n",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto","Con el objeto de que los niños se sientan seguros y se adapten con mayor facilidad en su\n" +
                "estancia en el colegio, durante las primeras dos semanas podrán traer consigo su objeto de\n" +
                "Apego, como puede ser: un trapito, una almohadita o un pequeño juguete. (UNICAMENTE\n" +
                "PARA LOS GRADOS DE k-I Y II) es necesario de igual forma traer su material de aseo cambio de ropa,\n" +
                "pañales y crema en caso de usarla. No está permitido el uso de biberones.\n" +
                "Le pedimos por favor cada niño de preescolar traiga su mantel individual y su vaso entrenador o\n" +
                "termo con popote.\n",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion"," El alumno tendrá derecho a dos retardos en el mes, a la Acumulación del tercer retardo el alumno\n" +
                "se hará acreedor a un día de suspensión.\n" +
                "- El alumno podrá entrar al colegio fuera de la hora normal únicamente por causas de\n" +
                "Fuerza mayor, presentando su justificante por escrito al director, no por vía telefónica.\n" +
                "- El alumno no puede retirarse del plantel antes de la hora oficial de salida sin previo\n" +
                "Aviso y autorización de la directora.\n" +
                "Es importante su puntualidad ya que es un valor formativo que queremos inculcar en nuestros\n" +
                "Alumnos, además ocasiona trastornos organizativos en el Colegio.",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio","Les informamos que contamos con extensión de horario por la tarde, por lo que si consideran\n" +
                "necesario de Contratar el servicio de estancia o apoyo en tareas si así lo desean comunicándolo en\n" +
                "dirección es muy importante ser puntuales al recoger a sus hijos, en caso de sobrepasar el horario\n" +
                "más de 15 minutos se le acumulara el horario y al mes pagara Ud. En la administración su servicio.\n" +
                "Recuerde tenemos la mejor intención de cuidar a sus hijos pero tenemos horarios y servicios\n" +
                "establecidos.",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias","Les informamos la dinámica a seguir durante el proceso de entrada y salida de los niños, la cual se\n" +
                "llevará acabo de la siguiente manera:\n" +
                "LAS ENTRADA ES SOLO POR PORTON DELANTERO. LA SALIDA ES POR LOS DOS PORTONES. \n" +
                "Haremos una sola fila utilizando un carril, el segundo carril quedará libre para que los vehículos\n" +
                "Puedan continuar circulando, para reducir el tiempo de espera en la fila durante la entrada y la\n" +
                "Salida es importante que porten en el cristal delantero en la esquina superior derecha, de forma\n" +
                "Visible, Un tarjetón tamaño carta con el nombre y grupo de sus hijos. (Se colocará en línea para que\n" +
                "Ud. Lo imprima) Les recordamos traer SIEMPRE dicho tarjetón en el parabrisas de su automóvil, ya\n" +
                "que Únicamente entregaremos a los alumnos identificados con el tarjetón correspondiente. (ES\n" +
                "FUNDAMENTAL QUE EL TARJETON PERMANEZCA A LO LARGO DEL CICLO ESCOLAR). Los maestros\n" +
                "que estén de guardia ayudarán a sus hijos en el proceso de subirse o bajarse del Carro. Solicitamos\n" +
                "a los padres de familia que no se estacionen y no se bajen de los vehículos Para dejar o recoger a\n" +
                "sus hijos, con esto evitaremos conflictos de tránsito. Les pedimos que no Utilicen estos tiempos\n" +
                "para hacer consultas a las maestras ya que su atención estará enfocada en la seguridad de los niños.\n" +
                "Deberán acatar las indicaciones de nuestro personal para agilizar el tránsito, les pedimos Guardar\n" +
                "el debido respeto al personal del colegio, a los demás padres de familia y a los Alumnos al esperar\n" +
                "sin desesperarse, rebasar o tocar el claxon. (TENEMOS DOS AREAS DE ASCENSO PARA EL HORARIO\n" +
                "DE SALIDA) PORTON DELANTERO Y PORTON TRACERO.\n",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto","Les recordamos que el alumno deberá presentarse diariamente aseados Y limpios con el uniforme\n" +
                "completo, planchado en buen estado peinado y oloroso.\n" +
                "Todos los lunes los alumnos asisten de gala, por favor recuerde portar la corbata\n" +
                "Zapatos boleados; para evitar extravío y confusiones de prendas, deberán marcarlo con su nombre\n" +
                "y grupo, en caso de no traer la ropa marcada el colegio no se hace Responsable por pérdidas.\n" +
                "CHEQUE SUS HORARIOS PARA SABER QUE DIA PARTICIPA EN EDUCACION FISICA. ES INDISPENSABLE\n" +
                "PORTAR TENIS ESPECIALES PARA DEPORTES Y SU UNIFORME CORRECTO. (PANTS Y PLAYERA\n" +
                "DEPORTIVA)\n" +
                "EVITE ENVIAR JUGUETES, PRENDAS Y ELEMENTOS NO REQUERIDOS.\n" +
                "(REVISAR HORARIOSY REGLAMENTOS Y EL DOCUMENTO DE CADA AREA)",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion","El colegio tiene una metodología integral en donde el arte, el baile y la música son muy importantes\n" +
                "les pedimos por favor tenga esto en cuenta TENEMOS MULTIPLES ACTIVIDADES EN EL AÑO ESCOLAR\n" +
                "POR FAVOR VERIIFIQUE EL CALENDARIO ENVIADO EN EL SISTEMA Y EN SU CORREO el 15 de\n" +
                "septiembre esta Ud. Invitado a nuestro PRIMER homenaje. (Necesitaremos QUE SU PEQUEÑO\n" +
                "PORTE trajes típicos.)\n",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio","Cuando deseen sostener una entrevista con los maestros de grupo, coordinación, Departamento\n" +
                "psicopedagógico o dirección; favor de solicitar una cita en la administración del Colegio. Es\n" +
                "indispensable que respeten los horarios de las citas.\n",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Jose garcia","Se le informa a los padres que el viernes 16 hay reunion en la sala de conferencias","Como colegio bilingüe sabemos la importancia de incorporar a nuestros alumnos en Actividades que\n" +
                "les permitan hacer uso del idioma de una manera práctica, es por eso, que a partir del grado de\n" +
                "preescolar cursarán la clase de inglés de forma creativa y divertida hay obras escenificaciones y rolle\n" +
                "Playing donde la participación del alumno es obligatoria. En el área de arte se realizará desde\n" +
                "preescolar con énfasis en teatro nuestra meta es crear alumnos líderes y seguros de sí mismos. Para\n" +
                "proteger la Ropa durante la clase de artes u otra actividad creativa cada uno de los niños deberá\n" +
                "traer un mandil de cuadros verde o la bata pintor, deberán marcarla con su nombre; se enviará a\n" +
                "casa los viernes de cada semana para lavarla, no olviden regresarla al colegio.",R.drawable.hombre));
        listaPublicaciones.add(new Publicacion("Steve Jobs","El sistema va a estar en mantenimiento de 20:00 a 04:00 el dia martes 25 agosto","Es muy importante participe Ud. en la semana del personaje de la semana para preescolar y primaria\n" +
                "y semanalmente aporte reciclado, su hijo portara un carnet mensual donde llevará el control de lo\n" +
                "que recicla, esta carnet le permitirá aportar puntos en su rúbrica de participación social envié\n" +
                "plásticos, papel bond reciclado, periódicos o revistas. No se recicla servilletas, ni papel de uso\n" +
                "personal.\n" +
                "Esperamos su participación en diferentes proyectos sociales.\n" +
                "De preferencia envié termos con jugos o agua marcados, no use tetra pack, no lo reciben, envié lo\n" +
                "menos posible de desechables. Envié su vaso entrenador en caso de kínder 1.\n",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Seymour Skinner","Por favor informar a los padres de los nuevos requisitos de inscripcion","Los niños traerán diariamente su lunch dentro de una lonchera, marcada con su nombre y Grupo.\n" +
                "Favor de procurar que el lunch sea del agrado del niño, nutritivo y corresponda a una Cantidad que\n" +
                "ustedes consideren apropiada para que el niño se la coma con gusto. Recuerden Que es un\n" +
                "refrigerio, no sustituye ninguna de las comidas, los alumnos deben llegar al colegio Previamente\n" +
                "desayunados.\n" +
                "El lunch deberá venir preparado y listo para comerse, debido a la cantidad de niños no será posible\n" +
                "calentarles el lunch, ya que implicaría un retraso en su tiempo de recreo. Les recordamos que el\n" +
                "colegio cuenta con cafetería y ofrece menús balanceados, para brindarles un buen servicio, los\n" +
                "padres que estén interesados en recibir el lunch para sus hijos Deberán comprarlo mensualmente o\n" +
                "enviar dinero con sus hijos. (No se dan alimentos fiados)",R.drawable.mujer));
        listaPublicaciones.add(new Publicacion("Edna Krabappel","taller de formacion y educacion sexual para los padres todos los jueves de abril,mayo y junio","DURANTE ESTE CICLO SE IMPARTIRAN CAMBIOS EN DIFERENTES AREAS DE LA NUEVA REFORMA\n" +
                "EDUCATIVA ES MUY VALIOSO ASISTA A LA JUNTA DE INICIO DE CICLO PARA QUE CONOZCA UD.\n" +
                "ESTOS CAMBIOS. ",R.drawable.hombre));
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
