package com.gba.practicapaises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gba.practicapaises.databinding.FragmentDetallePaisBinding;
import com.gba.practicapaises.placeholder.PlaceholderContent;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetallePaisFragment extends Fragment {

    private FragmentDetallePaisBinding binding;
    private PlaceholderContent.Pais mPais;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPais = getArguments().getParcelable( "pais");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetallePaisBinding.inflate(inflater, container,  false);
        ImageView iv = binding.foto;
        iv.setImageResource(getImageId(mPais.foto));
        TextView tv = binding.descripcion;
        tv.setText(mPais.detalles);
        return binding.getRoot();
    }
    private int getImageId(String imagePath) {
        String imageName = imagePath.substring(imagePath.lastIndexOf("/")
        +1, imagePath.lastIndexOf("."));
        return getResources().getIdentifier(imageName,
        "drawable", getContext().getPackageName());
    }
}