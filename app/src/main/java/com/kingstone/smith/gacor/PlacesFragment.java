package com.kingstone.smith.gacor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kingstone.smith.gacor.data.GacorContract;

import java.net.URI;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlacesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PlacesAdapter.PlacesAdapterOnClickHandler{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int PLACE_PICKER_REQUEST = 1;
    private RecyclerView mRecyclerView;
    private PlacesAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    final PlacePicker.IntentBuilder mPlacePickerIntentBuilder = new PlacePicker.IntentBuilder();

    public static final String[] PLACES_PROJECTION = {
            GacorContract.PlaceEntry._ID,
            GacorContract.PlaceEntry.COLUMN_PLACE_NAME,
            GacorContract.PlaceEntry.COLUMN_PLACE_DETAIL,
            GacorContract.PlaceEntry.COLUMN_PLACE_LAT,
            GacorContract.PlaceEntry.COLUMN_PLACE_LANG
    };

    public static final int INDEX_PLACE_ID = 0;
    public static final int INDEX_PLACE_NAME = 1;
    public static final int INDEX_PLACE_DETAIL = 2;
    public static final int INDEX_PLACE_LAT = 3;
    public static final int INDEX_PLACE_LANG = 4;

    static final int ID_PLACE_LOADER = 44;

    //mock data
    private String[] myDataset = {"a", "b", "c"};

    //data
    private ArrayList<Places> mPlaces = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public PlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlacesFragment newInstance(String param1, String param2) {
        PlacesFragment fragment = new PlacesFragment();
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
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(mPlacePickerIntentBuilder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        // specify an adapter (see also next example)
//        mAdapter = new PlacesAdapter(myDataset);
//        mPlaces.add(new Places("Baturaja", "Sum-Sel", -4.126947, 104.164174));
        mAdapter = new PlacesAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(ID_PLACE_LOADER, null, this);

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
//                mEditTextLocation.setText(place.getName() + "\n" + place.getLatLng().toString());
                double lat = ((double)((int)(place.getLatLng().latitude * 1000000.0)))/1000000.0;
                double lng = ((double)((int)(place.getLatLng().longitude * 1000000.0)))/1000000.0;
//                mPlaces.add(new Places(
//                        place.getName().toString(),
//                        place.getAddress().toString(),
//                        lat,
//                        lng));

                // insert to db
                ContentValues values = new ContentValues();
                values.put(GacorContract.PlaceEntry.COLUMN_PLACE_NAME, place.getName().toString());
                values.put(GacorContract.PlaceEntry.COLUMN_PLACE_DETAIL, place.getAddress().toString());
                values.put(GacorContract.PlaceEntry.COLUMN_PLACE_LAT, lat);
                values.put(GacorContract.PlaceEntry.COLUMN_PLACE_LANG, lng);
                Uri newUri = getContext().getContentResolver().insert(GacorContract.PlaceEntry.CONTENT_URI, values);
                long id = ContentUris.parseId(newUri);
                if ( id > 0) {
                    Toast.makeText(getContext(), "Places added!", Toast.LENGTH_LONG).show();
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Insert failed! InsertErrorCode " + String.valueOf(id), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onMenuItemClick(long id) {
        Uri uri = ContentUris.withAppendedId(GacorContract.PlaceEntry.CONTENT_URI, id);

        getActivity().getContentResolver().delete(uri, null, null);
    }

    @Override
    public void onClick(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLngBounds latLngBounds = builder.include(latLng).build();

        try {
            startActivityForResult(mPlacePickerIntentBuilder.setLatLngBounds(latLngBounds).build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

//        Toast.makeText(getContext(), places.getName(), Toast.LENGTH_SHORT)
//                .show();
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {
            case ID_PLACE_LOADER:

                Uri PlaceUri = GacorContract.PlaceEntry.CONTENT_URI;

                String sortOrder = GacorContract.PlaceEntry._ID + " ASC";

                return new CursorLoader(getContext(),
                        PlaceUri,
                        PLACES_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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
