package com.gba.fragmentosestaticos;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentoB extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragmento_b, container, false);
        return view;
    }

    public void recibir(String dato) {
        TextView tv = getActivity().findViewById(R.id.mensaje);
        tv.setText(dato);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView tv = getActivity().findViewById(R.id.mensaje);
        String texto = tv.getText().toString();
        outState.putString("MENSAJE2", texto);
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String mensaje1 = savedInstanceState.getString("MENSAJE2");
            TextView tv = getActivity().findViewById(R.id.mensaje);
            tv.setText(mensaje1);

        }
    }
}
