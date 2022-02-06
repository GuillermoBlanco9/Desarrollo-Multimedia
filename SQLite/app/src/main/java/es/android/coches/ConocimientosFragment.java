package es.android.coches;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import es.android.coches.databinding.FragmentConocimientosBinding;
import es.android.coches.entidad.Pregunta;
import es.android.coches.servicio.implementacion.ServicioPreguntasSQLiteImpl;
import es.android.coches.servicio.implementacion.ServicioPreguntasXMLImpl;
import es.android.coches.servicio.interfaz.ServicioPreguntas;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ConocimientosFragment extends Fragment {

    private FragmentConocimientosBinding binding;

    List<Pregunta> preguntas;
    int respuestaCorrecta;
    int puntos;
    ServicioPreguntas servicio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        servicio = new ServicioPreguntasSQLiteImpl(getContext());
        try {
            preguntas = new ArrayList<>(servicio.generarPreguntas("coches.xml"));
            Collections.shuffle(preguntas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConocimientosBinding.inflate(inflater, container, false);

        try {
            presentarPregunta();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        binding.botonRespuesta.setOnClickListener(v -> {
            int seleccionado = binding.radioGroup.getCheckedRadioButtonId();
            CharSequence mensaje = seleccionado == respuestaCorrecta ? "¡Acertaste!" : "Fallaste";

            if(seleccionado == respuestaCorrecta)puntos++;

            Snackbar.make(v, mensaje, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Siguiente", v1 -> {
                        try {
                            presentarPregunta();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
            v.setEnabled(false);
        });

        return binding.getRoot();
    }

    private void presentarPregunta() throws IOException, JSONException {
        if (preguntas.size() > 0) {
            binding.botonRespuesta.setEnabled(true);

            int pregunta = new Random().nextInt(preguntas.size());

            Pregunta preguntaActual = preguntas.remove(pregunta);
            preguntaActual.setRespuetas(servicio.generarRespuestasPosibles(preguntaActual.getRespuestaCorrecta(), binding.radioGroup.getChildCount()));

            InputStream bandera = null;
            try {
                bandera = getContext().getAssets().open(preguntaActual.getFoto());
                binding.bandera.setImageBitmap(BitmapFactory.decodeStream(bandera));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // anadir
            binding.radioGroup.clearCheck();
            for (int i = 0; i < binding.radioGroup.getChildCount(); i++) {
                RadioButton radio = (RadioButton) binding.radioGroup.getChildAt(i);
                // comentar
                // radio.setChecked(false);
                CharSequence respuesta = preguntaActual.getRespuetas().get(i);
                if (respuesta.equals(preguntaActual.getRespuestaCorrecta()))
                    respuestaCorrecta = radio.getId();

                radio.setText(respuesta);
            }
        } else {
            binding.bandera.setVisibility(View.GONE);
            binding.radioGroup.setVisibility(View.GONE);
            binding.botonRespuesta.setVisibility(View.GONE);
            binding.textView.setText("¡Fin!\nTu Puntuación: "+puntos);

            FileInputStream fis = getContext().openFileInput("prueba");

            JSONObject objJson = new JSONObject();
            objJson.put("puntuacion_maxima",puntos);
            objJson.put("ultima_puntuacion",puntos);

            String jsonString = objJson.toString();


            salvarFichero("prueba",jsonString);




            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String fileContent = br.readLine();
            while (fileContent != null) {
                    sb.append(fileContent).append("\n");
                    fileContent = br.readLine();
            }
            br.close();
            // This responce will have Json Format String
            String responce = sb.toString();

            JSONObject imprimir = new JSONObject(responce);


            System.out.println(imprimir.get("puntuacion_maxima"));
        }
    }

    private void salvarFichero(String fichero, String texto) {
        FileOutputStream fos;
        try {
            fos = getContext().openFileOutput(fichero, Context.MODE_PRIVATE);
            fos.write(texto.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}