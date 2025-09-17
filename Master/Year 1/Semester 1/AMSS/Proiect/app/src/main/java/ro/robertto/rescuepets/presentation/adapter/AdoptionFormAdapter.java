package ro.robertto.rescuepets.presentation.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.databinding.ItemAdoptionFormBinding;
import ro.robertto.rescuepets.presentation.activities.AdoptionFormProfileActivity;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;

public class AdoptionFormAdapter extends RecyclerView.Adapter< AdoptionFormAdapter.AdoptionFormViewHolder > implements Filterable {
    private final @NonNull AppCompatActivity activity;
    private View itemView;

    private final List< AdoptionForm > adoptionForms;
    private final List< AdoptionForm > filteredAdoptionForms;
    private final RescuePetsViewModel rescuePetsViewModel;

    public AdoptionFormAdapter( @NonNull AppCompatActivity activity, boolean setOnClickListenerOnViewCards ) {
        this.activity = activity;
        this.adoptionForms = new ArrayList<>();
        this.filteredAdoptionForms = new ArrayList<>();
        this.rescuePetsViewModel = new ViewModelProvider( activity,
                new RescuePetsViewModelFactory( activity.getApplication() ) ).get( RescuePetsViewModel.class );

    }

    static class AdoptionFormViewHolder extends RecyclerView.ViewHolder {
        public ItemAdoptionFormBinding binding;

        public AdoptionFormViewHolder( @NonNull View itemView ) {
            super( itemView );
            binding = ItemAdoptionFormBinding.bind( itemView );
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private List< AdoptionForm > getFilteredResults( String constraint ) {
                List< AdoptionForm > filteredAdoptionForms = new ArrayList<>();
                synchronized ( adoptionForms ) {
                    for ( AdoptionForm adoptionForm : adoptionForms ) {
                        if ( adoptionForm.getNickName().toLowerCase().contains( constraint ) ) {
                            filteredAdoptionForms.add( adoptionForm );
                        } else if ( adoptionForm.getDate().toLowerCase().contains( constraint ) ) {
                            filteredAdoptionForms.add( adoptionForm );
                        } else if ( adoptionForm.getComment().toLowerCase().contains( constraint ) ) {
                            filteredAdoptionForms.add( adoptionForm );
                        } else if ( adoptionForm.getStatus() == null && constraint.toLowerCase().contains( activity.getString( R.string.pending ) ) ) {
                            filteredAdoptionForms.add( adoptionForm );
                        } else if ( adoptionForm.getStatus() && constraint.toLowerCase().contains( activity.getString( R.string.approved ) ) ) {
                            filteredAdoptionForms.add( adoptionForm );
                        } else if ( !adoptionForm.getStatus() && constraint.toLowerCase().contains( activity.getString( R.string.denied ) ) ) {
                            filteredAdoptionForms.add( adoptionForm );
                        } else {
                            rescuePetsViewModel.getPet( adoptionForm.getPetUid() ).observe( activity, new Observer< Pet >() {
                                @Override
                                public void onChanged( Pet pet ) {
                                    if ( pet != null ) {
                                        if ( pet.getName().toLowerCase().contains( constraint ) ) {
                                            filteredAdoptionForms.add( adoptionForm );
                                        } else if ( pet.getSpecies().toLowerCase().contains( constraint ) ) {
                                            filteredAdoptionForms.add( adoptionForm );
                                        } else if ( pet.getBreed().toLowerCase().contains( constraint ) ) {
                                            filteredAdoptionForms.add( adoptionForm );
                                        }
                                    }
                                }
                            } );
                        }
                    }
                }
                return filteredAdoptionForms;
            }

            @Override
            protected FilterResults performFiltering( CharSequence constraint ) {
                List< AdoptionForm > filteredAdoptionForms = ( constraint.length() == 0 ) ?
                        adoptionForms : getFilteredResults( constraint.toString().toLowerCase() );

                synchronized ( AdoptionFormAdapter.this.filteredAdoptionForms ) {
                    AdoptionFormAdapter.this.filteredAdoptionForms.clear();
                    AdoptionFormAdapter.this.filteredAdoptionForms.addAll( filteredAdoptionForms );
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
    public AdoptionFormViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        LayoutInflater inflater = LayoutInflater.from( activity );
        itemView = inflater.inflate( R.layout.item_adoption_form, parent, false );
        return new AdoptionFormViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( @NonNull AdoptionFormViewHolder holder, int position ) {
        AdoptionForm adoptionForm = filteredAdoptionForms.get( position );
        holder.binding.adopterNickname.setText( adoptionForm.getNickName() );
        holder.binding.adoptionFormDate.setText( adoptionForm.getDate() );
        Boolean status = adoptionForm.getStatus();
        if ( status == null )
            holder.binding.adoptionFormStatus.setText( R.string.pending );
        else if ( status )
            holder.binding.adoptionFormStatus.setText( R.string.approved );
        else
            holder.binding.adoptionFormStatus.setText( R.string.denied );


        if ( rescuePetsViewModel != null ) {
            rescuePetsViewModel.getPet( adoptionForm.getPetUid() ).observe( activity, new Observer< Pet >() {
                @Override
                public void onChanged( Pet pet ) {
                    if ( pet != null ) {
                        holder.binding.petName.setText( pet.getName() );
                        Glide.with( activity )
                                .load( pet.getProfileImage() )
                                .placeholder( R.drawable.profile_pic )
                                .into( holder.binding.petProfilePic );
                    }
                }
            } );
        }

        holder.itemView.findViewById( R.id.pet_card_view ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent( activity, AdoptionFormProfileActivity.class );
                Bundle bundle = new Bundle();
                bundle.putString( "adoptionFormUid", adoptionForm.getUid() ); //user_email
                intent.putExtras( bundle ); //Put your id to your next Intent
                activity.startActivity( intent );//cream o noua activitate pt utilizatorul specific
            }
        } );
    }

    @Override
    public int getItemCount() {
        return filteredAdoptionForms.size();
    }

    public void setAdoptionForms( @Nullable List< AdoptionForm > adoptionForms ) {
        synchronized ( this.adoptionForms ) {
            this.adoptionForms.clear();
            if ( adoptionForms != null )
                this.adoptionForms.addAll( adoptionForms );
            synchronized ( filteredAdoptionForms ) {
                filteredAdoptionForms.clear();
                filteredAdoptionForms.addAll( this.adoptionForms );
            }
        }
        notifyDataSetChanged();
    }
}
