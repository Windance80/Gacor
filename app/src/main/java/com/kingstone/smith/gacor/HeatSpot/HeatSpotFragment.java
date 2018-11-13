package com.kingstone.smith.gacor.HeatSpot;

import android.app.LauncherActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
//import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingstone.smith.gacor.GacorFragment;
import com.kingstone.smith.gacor.R;
import com.kingstone.smith.gacor.data.GacorContract;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeatSpotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeatSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeatSpotFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, HeatSpotAdapter.HeatSpotAdapterOnClickHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static final int ID_HEATSPOT_LOADER = 777;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private HeatSpotAdapter mHeatSpotAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    private OnFragmentInteractionListener mListener;

    public static final String[] HEATSPOT_PROJECTION = {
            GacorContract.HeatspotEntry._ID,
            GacorContract.HeatspotEntry.COLUMN_DATE,
            GacorContract.HeatspotEntry.COLUMN_NAME,
            GacorContract.HeatspotEntry.COLUMN_DETAIL,
            GacorContract.HeatspotEntry.COLUMN_LAT,
            GacorContract.HeatspotEntry.COLUMN_LANG
    };

    public static final int INDEX_HEATSPOT_ID = 0;
    public static final int INDEX_HEATSPOT_DATE = 1;
    public static final int INDEX_HEATSPOT_NAME = 2;
    public static final int INDEX_HEATSPOT_DETAIL = 3;
    public static final int INDEX_HEATSPOT_LAT = 4;
    public static final int INDEX_HEATSPOT_LANG = 5;


    public HeatSpotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeatSpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeatSpotFragment newInstance(String param1, String param2) {
        HeatSpotFragment fragment = new HeatSpotFragment();
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
        View view = inflater.inflate(R.layout.fragment_heat_spot, container, false);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHeatspotActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewHeatSpot);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        // specify an adapter (see also next example)m
//        mAdapter = new PlacesAdapter(myDataset);
//        mPlaces.add(new Places("Baturaja", "Sum-Sel", -4.126947, 104.164174));
        mHeatSpotAdapter = new HeatSpotAdapter(getContext(), this);
        mRecyclerView.setAdapter(mHeatSpotAdapter);

        getActivity().getSupportLoaderManager().initLoader(ID_HEATSPOT_LOADER, null, this);

        return view;
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onMenuItemClick() {

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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_HEATSPOT_LOADER:

                Uri uri = GacorContract.HeatspotEntry.CONTENT_URI;
                String sordOrder = GacorContract.HeatspotEntry.COLUMN_DATE + " ASC";

                return new CursorLoader(
                        getContext(),
                        uri,
                        HEATSPOT_PROJECTION,
                        GacorContract.HeatspotEntry.COLUMN_DATE + " IS NOT NULL",
                        null,
                        sordOrder
                        );

                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<ItemType> items = new ArrayList<>();

        // pull data from the cursor to ModelData list
        if (data.moveToFirst()) {
            List<ModelData> modelData = new ArrayList<>();
            do {
                Calendar calendar = Calendar.getInstance();
                long lDate = data.getLong(INDEX_HEATSPOT_DATE);
                calendar.setTimeInMillis(lDate);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

                modelData.add(new ModelData(dayOfWeek, lDate, data.getString(INDEX_HEATSPOT_NAME), day));
            } while (data.moveToNext());

            // put the ModelData list to TreeMap with dayOfWeek as key
            Map<Integer, List<ModelData>> map = new TreeMap<>();
            for (ModelData modelData1 : modelData) {
                List<ModelData> value = map.get(modelData1.getDayOfWeek());
                if (value == null) {
                    value = new ArrayList<>();
                    map.put(modelData1.getDayOfWeek(), value);
                }
                value.add(modelData1);
            }

            // put the map to wrapper class for header and list
            for (int dayOfWeek : map.keySet()) {
                List<ModelData> list =  map.get(dayOfWeek);
                ItemHeader header = new ItemHeader(dayOfWeek, list.get(0).getDay()); //to get the name of the day
                items.add(header);

                // sort the ModelData with time
                Collections.sort(list, new Comparator<ModelData>() {
                    @Override
                    public int compare(ModelData modelData, ModelData t1) {
                        return modelData.getTime().compareTo(t1.getTime());
                    }
                });

                for (ModelData modelData1 : list) {
                    ItemList item = new ItemList(modelData1);
                    items.add(item);
                }
            }
        }

        mHeatSpotAdapter.swapCursor(items);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mHeatSpotAdapter.swapCursor(null);
    }
}
