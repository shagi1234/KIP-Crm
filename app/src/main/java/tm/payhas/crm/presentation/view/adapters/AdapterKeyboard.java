package tm.payhas.crm.presentation.view.adapters;

import static tm.payhas.crm.domain.helpers.StaticMethods.dpToPx;
import static tm.payhas.crm.domain.helpers.StaticMethods.getWindowWidth;
import static tm.payhas.crm.domain.helpers.StaticMethods.setBackgroundDrawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tm.payhas.crm.R;
import tm.payhas.crm.presentation.view.activity.ActivityMain;
import tm.payhas.crm.presentation.view.fragment.FragmentChangePassword;
import tm.payhas.crm.presentation.view.fragment.FragmentProfile;
import tm.payhas.crm.domain.interfaces.PasswordInterface;
import tm.payhas.crm.data.localdb.preference.AccountPreferences;

public class AdapterKeyboard extends RecyclerView.Adapter<AdapterKeyboard.ViewHolder> {
    private Context context;
    private Activity activity;
    private EditText keyboard;
    private ArrayList<Integer> keyboardCount;
    private ArrayList<FrameLayout> togalajyklar;
    private LinearLayout circleLayout;
    private boolean isActivity;
    private FragmentChangePassword fragment;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private boolean change;
    private boolean isPasswordSwitch;
    private TextView text;
    private AccountPreferences accountPreferences;


    public AdapterKeyboard(Activity activity, Context context, EditText keyboard, ArrayList<Integer> keyboardCount, ArrayList<FrameLayout> togalajyklar, LinearLayout circleLay, boolean isActivity, FragmentChangePassword fragment, boolean change, TextView text, boolean isPasswordSwitch) {
        this.activity = activity;
        this.context = context;
        this.keyboard = keyboard;
        this.keyboardCount = keyboardCount;
        this.togalajyklar = togalajyklar;
        this.circleLayout = circleLay;
        this.isActivity = isActivity;
        this.fragment = fragment;
        this.change = change;
        this.isPasswordSwitch = isPasswordSwitch;
        this.text = text;
        accountPreferences = AccountPreferences.newInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_keyboard, parent, false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mainLayout.setLayoutParams(new ViewGroup.LayoutParams((getWindowWidth(activity) - dpToPx(100, context)) / 3, (getWindowWidth(activity) - dpToPx(100, context)) / 3));


        if (keyboardCount.get(holder.getAdapterPosition()) != null) {
            holder.keyboardCount.setVisibility(View.VISIBLE);
            holder.keyboardImg.setVisibility(View.GONE);
            holder.keyboardCount.setText(String.valueOf(keyboardCount.get(holder.getAdapterPosition())));

        } else {
            holder.keyboardCount.setVisibility(View.GONE);
            holder.keyboardImg.setVisibility(View.VISIBLE);
            if (holder.getAdapterPosition() == 9) {
                holder.keyboardImg.setImageResource(R.drawable.ic_pause_circle);
            } else if (holder.getAdapterPosition() == 11) {
                holder.keyboardImg.setImageResource(R.drawable.ic_delete);
            }
        }

        if (holder.getAdapterPosition() == 9) {
            if (isActivity) {
                if (accountPreferences.getFingerLock() == 1) {
                    holder.itemView.setVisibility(View.VISIBLE);
                } else {
                    holder.itemView.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        }


        holder.itemView.setOnClickListener(v -> {
            setColorListener();
            if (keyboardCount.get(holder.getAdapterPosition()) != null) {
                keyboard.setText(String.valueOf(keyboard.getText().toString().trim() + keyboardCount.get(holder.getAdapterPosition())));
            } else {
                if (holder.getAdapterPosition() == 9) {
                    if (biometricPrompt != null && promptInfo != null) {
                        if (accountPreferences.getFingerLock() == 1) {
                            biometricPrompt.authenticate(promptInfo);
                        }
                    }
                } else if (holder.getAdapterPosition() == 11) {

                    if (keyboard.getText().toString().trim().length() == 0) return;

                    keyboard.setText(keyboard.getText().toString().trim().substring(0, keyboard.getText().toString().trim().length() - 1));
                }
            }

            if (keyboard.getText().toString().trim().length() == 4) {

                if (accountPreferences.getPassword().equals(keyboard.getText().toString().trim())) {

                    if (isActivity) {
                        Intent intent;
                        intent = new Intent(activity, ActivityMain.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else if (change) {
                        if (isPasswordSwitch) {
                            accountPreferences.savePassword("");
                            FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.remove(fragment);
                            transaction.commit();
                            manager.popBackStack();
                        } else {
                            accountPreferences.savePassword("");
                            keyboard.setText("");
                            text.setText(activity.getResources().getString(R.string.enter_new_code));
                            for (int i = 0; i < 4; i++) {
                                setBackgroundDrawable(context, togalajyklar.get(i), R.color.color_transparent, R.color.primary, 0, true, 1);
                            }
                        }
                        Fragment profile = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentProfile.class.getSimpleName());
                        if (profile instanceof PasswordInterface) {
                            ((PasswordInterface) profile).setEnabled();
                        }

                    } else {
                        Intent intent;
                        intent = new Intent(activity, ActivityMain.class);
                        activity.startActivity(intent);
                        activity.finish();

                    }
                } else if (accountPreferences.getPassword().equals("")) {
                    accountPreferences.savePassword(keyboard.getText().toString().trim());
                    Fragment profile = ActivityMain.mainFragmentManager.findFragmentByTag(FragmentProfile.class.getSimpleName());
                    if (profile instanceof PasswordInterface) {
                        ((PasswordInterface) profile).setEnabled();
                    }
                    if (change) {
                        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.remove(fragment);
                        transaction.commit();
                        manager.popBackStack();
                    }

                } else {
                    keyboard.setText("");
                    for (int i = 0; i < 4; i++) {
                        setBackgroundDrawable(context, togalajyklar.get(i), R.color.color_transparent, R.color.primary, 0, true, 1);
                    }
                    Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);

                    circleLayout.startAnimation(shake);
                    Vibrator vb = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vb.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vb.vibrate(300);
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return keyboardCount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView keyboardCount;
        private FrameLayout mainLayout;
        private AppCompatImageView keyboardImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            keyboardCount = itemView.findViewById(R.id.keyboard_count);
            mainLayout = itemView.findViewById(R.id.main_container);
            keyboardImg = itemView.findViewById(R.id.keyboard_img);

        }
    }

    private void setColorListener() {
        keyboard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeColor(s.length() - 1);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void changeColor(int length) {
        for (int i = 0; i < 4; i++) {
            if (i <= length) {
                setBackgroundDrawable(context, togalajyklar.get(i), R.color.primary, 0, 0, true, 0);
            } else {
                setBackgroundDrawable(context, togalajyklar.get(i), R.color.color_transparent, R.color.primary, 0, true, 1);
            }
        }
    }


}
