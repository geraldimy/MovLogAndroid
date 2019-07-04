package com.example.rog.movlog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapterGenre extends FragmentStatePagerAdapter {

    String[] tabarray = new String[]{"Action","Adventure","Comedy","Fantasy","Horror","Mystery","Romantic"};
    Integer tabnumber = 7;

    public TabPagerAdapterGenre(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                GenreAction genreAction = new GenreAction();
                return  genreAction;
            case 1:
                GenreAdventure genreAdventure = new GenreAdventure();
                return  genreAdventure;
            case 2:
                GenreComedy genreComedy = new GenreComedy();
                return  genreComedy;
            case 3:
                GenreFantasy genreFantasy = new GenreFantasy();
                return  genreFantasy;
            case 4:
                GenreHorror genreHorror = new GenreHorror();
                return  genreHorror;
            case 5:
                GenreMystery genreMystery = new GenreMystery();
                return  genreMystery;
            case 6:
                GenreRomantic genreRomantic = new GenreRomantic();
                return  genreRomantic;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabnumber;
    }
}
