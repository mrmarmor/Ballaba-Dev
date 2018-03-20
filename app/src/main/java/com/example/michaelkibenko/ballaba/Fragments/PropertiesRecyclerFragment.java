package com.example.michaelkibenko.ballaba.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michaelkibenko.ballaba.Adapters.PropertiesRecyclerAdapter;
import com.example.michaelkibenko.ballaba.Entities.BallabaProperty;
import com.example.michaelkibenko.ballaba.Entities.BallabaUser;
import com.example.michaelkibenko.ballaba.Managers.PropertiesManager;
import com.example.michaelkibenko.ballaba.Presenters.SearchPropertiesPresenter;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.MainScreenLayoutBinding;

import java.util.List;

public class PropertiesRecyclerFragment extends Fragment {
    private final String TAG = PropertiesRecyclerFragment.class.getSimpleName();
    private static final String PROPERTIES_KEY = "properties key";

    // TODO: Rename and change types of parameters
    //private List<BallabaProperty> mParam;
    private PropertiesRecyclerAdapter rvAdapter;
    private RecyclerView rvProperties;
    private List<BallabaProperty> properties;

    private SearchPropertiesPresenter presenter;
    private static MainScreenLayoutBinding binder;
    private LayoutInflater inflater;

    private OnFragmentInteractionListener mListener;

    public PropertiesRecyclerFragment() {}

    // TODO: Rename and change types and number of parameters
    public static PropertiesRecyclerFragment newInstance(MainScreenLayoutBinding mBinder, String param) {
        binder = mBinder;
        PropertiesRecyclerFragment fragment = new PropertiesRecyclerFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROPERTIES_KEY, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binder = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_properties_recycler);

        //binder.setPresenter(presenter);

        if (getArguments() != null) {
            //get arguments here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_properties_recycler, container, false);
        this.inflater = inflater;
        //DataBindingUtil.inflate(
        //        inflater, R.layout.fragment_properties_recycler, null, false);
        //presenter = new SearchPropertiesPresenter(getActivity(), binder, mParam);

        initRecycler(v);

        return v;//binder.getRoot();
    }

    private void initRecycler(View view) {
        properties = PropertiesManager.getInstance(getContext()).getProperties();//new ArrayList<BallabaProperty>();
        //TODO moving binder across fragments when a specific widget is in the child fragment and
        //TODO not in the parent activity cause binder not be able to see the specific widget.
        //TODO that is why binder.getRoot() returns null. use findViewById instead!!!
        //rvProperties = (RecyclerView)binder.getRoot().findViewById(R.id.properties_recycler_RV);

        rvProperties = (RecyclerView)view.findViewById(R.id.properties_recycler_RV);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        arrangeLayoutManagerToDisplaySingleAndDuplicateItems(manager);
        rvAdapter = new PropertiesRecyclerAdapter(getContext(), rvProperties, manager, properties, new BallabaUser());
        rvProperties.setLayoutManager(manager);
        rvProperties.setAdapter(rvAdapter);
    }

    private void arrangeLayoutManagerToDisplaySingleAndDuplicateItems(final GridLayoutManager layoutManager){
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position > 0) {
                    return 1;
                }

                return 2;
            }
        });
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

        /*DataBindingUtil.inflate(
                inflater, R.layout.fragment_properties_recycler, null, false);
        presenter = new SearchPropertiesPresenter(getActivity(), binder, mParam);
*/
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
