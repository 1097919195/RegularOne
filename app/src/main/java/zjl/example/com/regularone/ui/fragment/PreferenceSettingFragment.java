package zjl.example.com.regularone.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import zjl.example.com.regularone.R;

public class PreferenceSettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    //修改背景色
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        ListView lv = (ListView)view.findViewById(android.R.id.list);
//        lv.setBackgroundColor(getResources().getColor(R.color.alpha_05_black));
//        return view;
//    }

    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        prefs=PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean checkbox=prefs.getBoolean("checkbox_preference",false);
        Toast.makeText(getActivity(), ""+checkbox, Toast.LENGTH_SHORT).show();

        CheckBoxPreference cp=(CheckBoxPreference)findPreference("checkbox_preference");
        SwitchPreference sp=(SwitchPreference)findPreference("switch_preference");
        cp.setOnPreferenceClickListener(this);
        sp.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast.makeText(getActivity(), "开关按钮2的值是："+newValue, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Boolean checkboxValue=prefs.getBoolean(preference.getKey(),false);
        Toast.makeText(getActivity(), "开关按钮1的值是："+checkboxValue, Toast.LENGTH_SHORT).show();
        return true;
    }

}
