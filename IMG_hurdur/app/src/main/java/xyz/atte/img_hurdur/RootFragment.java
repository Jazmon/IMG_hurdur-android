package xyz.atte.img_hurdur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class is used as a root fragment which content is changed constantly.
 *
 * @Author Mikko Tossavainen
 * @Author Atte Huhtakangas
 * @Version 1.0
 */
public class RootFragment extends Fragment {
    /**
     * Fragment gets initialized and replaced with new fragment.
     * <br>
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.rootfragment, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.root_frame, new BrowseFragment());

        transaction.commit();

        return view;
    }

}
