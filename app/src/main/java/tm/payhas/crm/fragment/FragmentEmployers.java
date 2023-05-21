package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setPadding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tm.payhas.crm.R;

public class FragmentEmployers extends Fragment {
    // TODO: Rename and change types and number of parameters
    public static FragmentEmployers newInstance() {
        FragmentEmployers fragment = new FragmentEmployers();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employers, container, false);
    }
}