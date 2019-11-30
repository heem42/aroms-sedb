package se.aroms;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ReportAdapter extends FragmentStatePagerAdapter {

    int countTab;
    public ReportAdapter(@NonNull FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            WeeklyReportFragment f = new WeeklyReportFragment();
            return f;
        }
        else if(position == 1){
            MonthlyReportFragment f = new MonthlyReportFragment();
            return f;
        }
        else if(position == 2){
            YearlyReportFragment f = new YearlyReportFragment();
            return f;
        }
        else return null;
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
