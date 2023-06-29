package tm.payhas.crm.fragment;

import static tm.payhas.crm.helpers.StaticMethods.setPadding;
import static tm.payhas.crm.helpers.StaticMethods.statusBarHeight;
import static tm.payhas.crm.statics.StaticConstants.HOLIDAYS;
import static tm.payhas.crm.statics.StaticConstants.NEWS;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tm.payhas.crm.R;
import tm.payhas.crm.adapters.AdapterAddImage;
import tm.payhas.crm.api.data.DataImages;
import tm.payhas.crm.api.request.RequestNews;
import tm.payhas.crm.api.response.ResponseAddNews;
import tm.payhas.crm.databinding.FragmentAddNewsBinding;
import tm.payhas.crm.helpers.Common;
import tm.payhas.crm.interfaces.UploadedFilesUrl;
import tm.payhas.crm.preference.AccountPreferences;

public class FragmentAddNews extends Fragment implements UploadedFilesUrl {
    private FragmentAddNewsBinding b;
    private AccountPreferences ac;
    private ArrayList<DataImages> images = new ArrayList<>();
    private AdapterAddImage adapterAddImage;
    private String typeNews = "";

    public static FragmentAddNews newInstance() {
        FragmentAddNews fragment = new FragmentAddNews();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentAddNewsBinding.inflate(inflater);
        setRecycler();
        setSelector();
        setUpHelpers();
        setSpinner();
        initListeners();

        return b.getRoot();
    }

    private void setRecycler() {
        adapterAddImage = new AdapterAddImage(getContext());
        b.rvImages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        b.rvImages.setAdapter(adapterAddImage);
    }

    private void setSelector() {
        DataImages selector = new DataImages("noImage");
        selector.setSelector(true);
        images.add(selector);
        adapterAddImage.setImages(images);
    }

    private void setUpHelpers() {
        ac = new AccountPreferences(getContext());
    }

    private void setSpinner() {
        String[] types = getResources().getStringArray(R.array.dashboard_type);
        ArrayAdapter adapterType = new ArrayAdapter(getContext(), R.layout.item_spinner, types);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerType.setAdapter(adapterType);
    }

    private void initListeners() {
        b.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChecklist();
            }
        });
        b.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                switch (selected) {
                    case "Tazelik":
                        typeNews = NEWS;
                        break;
                    case "Bayramcylyk":
                        typeNews = HOLIDAYS;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonActivity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonActivity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.edtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonActivity();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.btnSave.setOnClickListener(view -> {
            b.btnSave.setEnabled(false);
            b.progressBar.setVisibility(View.VISIBLE);
            b.inputContent.setAlpha(0.5f);
            addNewsToDashboard();
        });
        b.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        b.edtDate.setOnClickListener(view -> openDialog(b.edtDate));

    }

    private void addNewChecklist() {
    }

    private void addNewsToDashboard() {
        ArrayList<String> imagesToUpload = new ArrayList<>();
        RequestNews requestNews = new RequestNews();
        requestNews.setAuthorId(ac.getAuthorId());
        requestNews.setTitle(b.edtName.getText().toString());
        requestNews.setContent(b.edtDescription.getText().toString());
        for (int i = 0; i < images.size(); i++) {
            if (!(images.get(i).isSelector())) {
                imagesToUpload.add(images.get(i).getImageUrl());
            }
        }
        requestNews.setAttachments(imagesToUpload);
        requestNews.setType(typeNews);
        Call<ResponseAddNews> call = Common.getApi().addNewsToDashboard(ac.getToken(), requestNews);
        call.enqueue(new Callback<ResponseAddNews>() {
            @Override
            public void onResponse(Call<ResponseAddNews> call, Response<ResponseAddNews> response) {
                if (response.isSuccessful()) {
                    b.btnSave.setEnabled(true);
                    b.progressBar.setVisibility(View.GONE);
                    b.inputContent.setAlpha(1);
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }
                b.progressBar.setVisibility(View.GONE);
                b.inputContent.setAlpha(1);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseAddNews> call, Throwable t) {
                Log.e("NEWS", "onFailure: " + t.getMessage());
                b.btnSave.setEnabled(true);
                b.progressBar.setVisibility(View.GONE);
                b.inputContent.setAlpha(1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> setPadding(b.main,
                0,
                statusBarHeight,
                0,
                0), 100);
    }

    private void buttonActivity() {
        b.btnSave.setEnabled(b.edtName.getText().length() > 0 && b.edtDescription.getText().length() > 0);
    }

    private void openDialog(TextView dateSet) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> dateSet.setText(String.valueOf(day1) + "/" + String.valueOf(month1 + 1) + "/" + String.valueOf(year1)), year, month, day);
        datePickerDialog.show();
    }


    @Override
    public void onUploadManyFiles(ArrayList<String> dataFiles) {
        for (int i = 0; i < dataFiles.size(); i++) {
            images.add(new DataImages(dataFiles.get(i)));
        }
        adapterAddImage.setImages(images);
    }

    @Override
    public void deleteSelectedImage(DataImages image) {
        images.remove(image);
    }
}