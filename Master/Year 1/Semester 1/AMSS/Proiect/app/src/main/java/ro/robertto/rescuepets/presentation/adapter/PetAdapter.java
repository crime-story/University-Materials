package ro.robertto.rescuepets.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.databinding.ItemPetBinding;
import ro.robertto.rescuepets.presentation.activities.PetProfileActivity;

public class PetAdapter extends RecyclerView.Adapter< PetAdapter.PetViewHolder > implements Filterable {
    private final Context context;
    private View itemView;

    private final List< Pet > mPets;
    private final List< Pet > mFilteredPets;

    public PetAdapter( Context context, boolean setOnClickListenerOnViewCards ) {
        this.context = context;
        this.mPets = new ArrayList<>();
        this.mFilteredPets = new ArrayList<>();
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {
        public ItemPetBinding binding;

        public PetViewHolder( @NonNull View itemView ) {
            super( itemView );
            binding = ItemPetBinding.bind( itemView );
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private List< Pet > getFilteredResults( String constraint ) {
                List< Pet > filteredPets = new ArrayList<>();
                synchronized ( mPets ) {
                    for ( Pet pet : mPets ) {
                        if ( pet.getName().toLowerCase().contains( constraint ) ) {
                            filteredPets.add( pet );
                        } else if ( pet.getBreed().toLowerCase().contains( constraint ) ) {
                            filteredPets.add( pet );
                        } else if ( pet.getSpecies().toLowerCase().contains( constraint ) ) {
                            filteredPets.add( pet );
                        } else if ( String.valueOf( pet.getBirthYear() ).toLowerCase().contains( constraint ) ) {
                            filteredPets.add( pet );
                        } else if ( pet.getDescription().toLowerCase().contains( constraint ) ) {
                            filteredPets.add( pet );
                        }
                    }
                }
                return filteredPets;
            }

            @Override
            protected FilterResults performFiltering( CharSequence constraint ) {
                List< Pet > filteredPets = ( constraint.length() == 0 ) ?
                        mPets : getFilteredResults( constraint.toString().toLowerCase() );

                synchronized ( mFilteredPets ) {
                    mFilteredPets.clear();
                    mFilteredPets.addAll( filteredPets );
                }

                return new FilterResults();
            }

            @Override
            protected void publishResults( CharSequence constraint, FilterResults results ) {
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        LayoutInflater inflater = LayoutInflater.from( context );
        itemView = inflater.inflate( R.layout.item_pet, parent, false );
        return new PetViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( @NonNull PetViewHolder holder, int position ) {
        Pet pet = mFilteredPets.get( position );
        holder.binding.petName.setText( pet.getName() );
        holder.binding.petBreed.setText( pet.getBreed() );
        holder.binding.petSpecies.setText( pet.getSpecies() );
        holder.binding.petYearOfBirth.setText( String.valueOf( pet.getBirthYear() ) );
        Glide.with( context )
                .load( pet.getProfileImage() )
                .placeholder( R.drawable.profile_pic )
                .into( holder.binding.petProfilePic );
        holder.itemView.findViewById( R.id.pet_card_view ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent( context, PetProfileActivity.class );
                Bundle bundle = new Bundle();
                bundle.putString( "petUid", pet.getUid() ); //user_email
                intent.putExtras( bundle ); //Put your id to your next Intent
                context.startActivity( intent );//cream o noua activitate pt utilizatorul specific

            }
        } );
    }

    @Override
    public int getItemCount() {
        return mFilteredPets.size();
    }

    public void setPets( @Nullable List< Pet > pets ) {
        synchronized ( mPets ) {
            mPets.clear();
            if ( pets != null )
                mPets.addAll( pets );
            synchronized ( mFilteredPets ) {
                mFilteredPets.clear();
                mFilteredPets.addAll( mPets );
            }
        }
        notifyDataSetChanged();
    }
}
