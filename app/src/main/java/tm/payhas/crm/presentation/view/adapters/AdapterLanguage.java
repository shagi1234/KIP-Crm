package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.domain.helpers.LanguageManager.LANG_RU;
import static tm.payhas.crm.domain.helpers.LanguageManager.LANG_TK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import tm.payhas.crm.R;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.domain.helpers.LanguageManager;

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.ViewHolder> {

    private Context context;
    private ArrayList<String> languages = new ArrayList<>();
    private Activity activity;

    public AdapterLanguage(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String language = languages.get(position);
        holder.bind(language);
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView language;
        private LanguageManager languageManager;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            language = itemView.findViewById(R.id.lay_language);
            languageManager = LanguageManager.newInstance(context);
        }

        public void bind(String languageText) {
            language.setText(languageText);
            language.setOnClickListener(view -> {
                if (languageText.equals("Pусский язык")) {
                    if (languageManager.getLanguage().equals("ru")) {
                        if (activity != null) {
                            activity.onBackPressed();
                            return;
                        }
                    }
                    setLocale("ru");
                    languageManager.setLanguage(LANG_RU);
                    languageManager.setLanguage(LANG_RU);
                } else {
                    if (languageManager.getLanguage().equals("tk")) {
                        if (activity != null) {
                            activity.onBackPressed();
                            return;
                        }
                    }
                    setLocale("tk");
                    languageManager.setLanguage(LANG_TK);
                    languageManager.setLanguage(LANG_TK);
                }
                Log.e("LANGUAGE", "onClickedLanguage: " +languageManager.getLanguage());
                activity.finish();
                activity.startActivity(new Intent(context, ActivityMain.class));
            });
        }

        public void setLocale(String lang) {
            Locale myLocale = new Locale(lang);
            Resources res = activity.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }
}