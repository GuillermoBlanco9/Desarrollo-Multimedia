package com.gba.pokedexgrupo1;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gba.pokedexgrupo1.databinding.FragmentItemDetailBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {



    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    public static final String ARG_ITEM_NAME = "item_name";
    public static final String ARG_DESCRIPTION = "item_description";
    public static final Integer ARG_ITEM_ID=0;

    private Integer mItemId;
    private String mItemName;
    private String mItemDescrption;


    /**
     * The placeholder content this fragment is presenting.
     */
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mTextView;
    private FragmentItemDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID.toString())) {
            mItemId = getArguments().getInt(ARG_ITEM_ID.toString());
            mItemName = getArguments().getString(ARG_ITEM_NAME);
            mItemDescrption = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentItemDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        mToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        mTextView = binding.itemDetail;

        ((TextView) rootView.findViewById(R.id.item_detail)).setText(
                getActivity().getIntent().getStringExtra(ARG_DESCRIPTION)
        );

        // Show the placeholder content as text in a TextView & in the toolbar if available.
        updateContent();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (mItemId != null) {
            mTextView.setText(mItemDescrption);
            if (mToolbarLayout != null) {
                mToolbarLayout.setTitle(mItemName);
            }
            ImageView itemImage = binding.itemImage;
            String pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + mItemId + ".png";
            Glide.with(this)
                    .load(pokemonImageUrl)
                    .into(itemImage);
        }
    }
}