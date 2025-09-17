package ro.robertto.rescuepets.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ro.robertto.rescuepets.R;

public class LocationDetailsFragment extends Fragment {
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView phoneTextView;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_location_details, container, false );

        nameTextView = view.findViewById( R.id.nameTextView );
        addressTextView = view.findViewById( R.id.addressTextView );
        phoneTextView = view.findViewById( R.id.phoneTextView );

        // Get the location details from the arguments passed in
        Bundle arguments = getArguments();
        if ( arguments != null ) {
            String locationName = arguments.getString( "name" );
            String locationAddress = arguments.getString( "address" );
            String locationPhone = arguments.getString( "phone" );

            // Set the UI element values with the location details
            if ( nameTextView != null )
                nameTextView.setText( locationName );
            if ( addressTextView != null )
                addressTextView.setText( locationAddress );
            if ( phoneTextView != null )
                phoneTextView.setText( locationPhone );
        }

        return view;
    }
}
