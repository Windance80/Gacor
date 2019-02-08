package com.kingstone.smith.gacor.Gacor;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kingstone.smith.gacor.HeatSpot.HeatSpotFragment;
import com.kingstone.smith.gacor.ItemType;
import com.kingstone.smith.gacor.R;
import com.kingstone.smith.gacor.data.GacorContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GacorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GacorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GacorFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, GacorAdapter.GacorAdapterOnClickHandler{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private Activity mActivity;
    private int mType; // 0 = place, 1 = time {For Gacor POJO}

    private GacorAdapter mAdapter;
    private List<ItemType> mItems;
    private RecyclerView mRecyclerView;

    private static final int REQUEST_CODE_COARSE = 777;
    private static final int REQUEST_CODE_FINE = 666;

    private OnFragmentInteractionListener mListener;

    public static final int ID_GACOR_LOADER = 14;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public GacorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GacorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GacorFragment newInstance(String param1, String param2) {
        GacorFragment fragment = new GacorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gacor, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerViewGacor);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mItems = new ArrayList<>();
        mAdapter = new GacorAdapter(mContext, this, mItems);
        mRecyclerView.setAdapter(mAdapter);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

        getActivity().getSupportLoaderManager().initLoader(ID_GACOR_LOADER, null, this);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_COARSE:
                break;
            case REQUEST_CODE_FINE:
                break;
            default:
                Toast.makeText(mContext, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_GACOR_LOADER:
                Uri queryUri = GacorContract.HeatspotEntry.CONTENT_URI;
                String sortOrder = GacorContract.HeatspotEntry._ID + " ASC";
//                String selection = GacorContract.HeatspotEntry.COLUMN_DATE + " IS NULL";
//                String selection = GacorContract.HeatspotEntry.COLUMN_DATE + " IS NULL OR strftime('%w', " + GacorContract.HeatspotEntry.COLUMN_DATE + ") = '1'";
//                String selection = GacorContract.HeatspotEntry.COLUMN_DATE + " IS NULL OR strftime('%w', " + GacorContract.HeatspotEntry.COLUMN_DATE + ") = '" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + "'";
                // Query all data and filter it later in Cursor onLoadFinished
                return new CursorLoader(mContext,
                        queryUri,
                        HeatSpotFragment.HEATSPOT_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull final android.support.v4.content.Loader<Cursor> loader, final Cursor data) {
        if (isAdded()) {
            if (data.moveToFirst()) {
                //request device location permission
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE);
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_COARSE);
                } else {

                    mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // calculate distance
                            if (location != null ) {
                                List<Gacor> gacors = new ArrayList<>();
                                mItems.clear();

                                try {
                                    Location location1 = new Location("pointA");
                                    location1.setLatitude(data.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LAT));
                                    location1.setLongitude(data.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LANG));

                                    double distance = location.distanceTo(location1);

                                    Calendar calendar = Calendar.getInstance();
                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    long date = data.getLong(HeatSpotFragment.INDEX_HEATSPOT_DATE);
                                    calendar.setTimeInMillis(date);

                                    // Filter the data so that it display only data from today DAY_OF_WEEK or if date is null for fav. places
                                    if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek || date == 0) {
                                        String time = "";
                                        if (date != 0) {
                                            time = new SimpleDateFormat("HH:mm").format(data.getLong(HeatSpotFragment.INDEX_HEATSPOT_DATE));
                                            mType = 0;
                                        } else {
                                            mType = 1;
                                        }

                                        gacors.add(
                                                new Gacor(
                                                        data.getString(HeatSpotFragment.INDEX_HEATSPOT_NAME),
                                                        String.valueOf(((int) (distance / 100.0)) / 10.0),
                                                        time,
                                                        mType
                                                )
                                        );
                                    }

                                    while (data.moveToNext()) {
                                        location1.setLatitude(data.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LAT));
                                        location1.setLongitude(data.getDouble(HeatSpotFragment.INDEX_HEATSPOT_LANG));

                                        distance = location.distanceTo(location1);
                                        date = data.getLong(HeatSpotFragment.INDEX_HEATSPOT_DATE);
                                        calendar.setTimeInMillis(date);

                                        if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek || date == 0) {
                                            String time = "";
                                            if (date != 0) {
                                                time = new SimpleDateFormat("HH:mm").format(data.getLong(HeatSpotFragment.INDEX_HEATSPOT_DATE));
                                                mType = 0;
                                            } else {
                                                mType = 1;
                                            }
                                            gacors.add(
                                                    new Gacor(
                                                            data.getString(HeatSpotFragment.INDEX_HEATSPOT_NAME),
                                                            String.valueOf(((int) (distance / 100.0)) / 10.0),
                                                            time,
                                                            mType
                                                    )
                                            );
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // put the list to map with type as key
                                Map<Integer, List<Gacor>> map = new TreeMap<>();
                                for (Gacor gacor : gacors) {
                                    List<Gacor> value = map.get(gacor.getType());
                                    if (value == null) {
                                        value = new ArrayList<>();
                                        map.put(gacor.getType(), value);
                                    }
                                    value.add(gacor);
                                }

                                // put the map into wrapper class for header and list
                                for (int type : map.keySet()) {
                                    List<Gacor> list = map.get(type);
                                    GacorHeader header = new GacorHeader(list.get(0).getType());
                                    mItems.add(header);

                                    Collections.sort(list, new Comparator<Gacor>() {
                                        @Override
                                        public int compare(Gacor gacor, Gacor t1) {
                                            return gacor.getTime().compareTo(t1.getTime());
                                        }
                                    });

                                    for (Gacor gacor : list) {
                                        GacorList item = new GacorList(gacor);
                                        mItems.add(item);
                                    }
                                }

                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
    }

    @Override
    public void onClick() {

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
