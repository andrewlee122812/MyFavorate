package com.silverforge.busyindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silverforge.controls.BusyIndicator;

public class CircleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        BusyIndicator circleBusyIndicator = (BusyIndicator) view.findViewById(R.id.finiteCircleBusyIndicator);

        circleBusyIndicator.setMaxValue(102);
        new BusyIndicatorAsyncTask(getActivity(), circleBusyIndicator).execute();

        return view;
    }    

}
