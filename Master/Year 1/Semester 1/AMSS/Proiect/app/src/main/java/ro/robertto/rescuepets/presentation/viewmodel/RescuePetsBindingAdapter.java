package ro.robertto.rescuepets.presentation.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityAdoptionFormProfileBinding;
import ro.robertto.rescuepets.databinding.ActivityCenterInfoBinding;
import ro.robertto.rescuepets.databinding.ActivityPetProfileBinding;
import ro.robertto.rescuepets.presentation.activities.PetAdoptActivity;
import ro.robertto.rescuepets.presentation.activities.PetProfileEditActivity;
import ro.robertto.rescuepets.presentation.adapter.AdoptionFormAdapter;
import ro.robertto.rescuepets.presentation.adapter.PetAdapter;
import timber.log.Timber;

public final class RescuePetsBindingAdapter {

    @BindingAdapter( "setPetSearchViewFilter" )
    public static void setPetSearchViewFilter( @NonNull RecyclerView mRecyclerView, @Nullable SearchView searchView ) {
        if ( searchView != null ) {
            PetAdapter petAdapter = ( PetAdapter ) mRecyclerView.getAdapter();
            if ( petAdapter != null ) {
                searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit( String query ) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange( String newText ) {
                        petAdapter.getFilter().filter( newText );
                        return false;
                    }
                } );

            }
        }
    }

    @BindingAdapter( "setFormsSearchViewFilter" )
    public static void setFormsSearchViewFilter( @NonNull RecyclerView mRecyclerView, @Nullable SearchView searchView ) {
        if ( searchView != null ) {
            AdoptionFormAdapter adapter = ( AdoptionFormAdapter ) mRecyclerView.getAdapter();
            if ( adapter != null ) {
                searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit( String query ) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange( String newText ) {
                        adapter.getFilter().filter( newText );
                        return false;
                    }
                } );

            }
        }
    }

    @BindingAdapter( { "recycleViewPetInit" } )
    public static void recycleViewPetInit( @NonNull RecyclerView mRecyclerView, boolean setOnClickListenerOnViewCards ) {
        if ( mRecyclerView.getAdapter() == null ) {
            // setam adaptorul pentru fiecare joc
            PetAdapter petAdapter = new PetAdapter( mRecyclerView.getContext(), setOnClickListenerOnViewCards );
            mRecyclerView.setAdapter( petAdapter );
        }
        if ( mRecyclerView.getLayoutManager() == null ) {
            // setam layout managerul
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( mRecyclerView.getContext() );
            mRecyclerView.setLayoutManager( layoutManager );
        }
    }

    @BindingAdapter( { "recycleViewFormInit", "activity" } )
    public static void recycleViewFormInit( @NonNull RecyclerView mRecyclerView, @NonNull AppCompatActivity activity, boolean setOnClickListenerOnViewCards ) {
        if ( mRecyclerView.getAdapter() == null ) {
            // setam adaptorul pentru fiecare joc
            AdoptionFormAdapter adapter = new AdoptionFormAdapter( activity, setOnClickListenerOnViewCards );
            mRecyclerView.setAdapter( adapter );
        }
        if ( mRecyclerView.getLayoutManager() == null ) {
            // setam layout managerul
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( mRecyclerView.getContext() );
            mRecyclerView.setLayoutManager( layoutManager );
        }
    }

    @BindingAdapter( { "recycleViewPetsBind", "activity" } )
    public static void recycleViewPetsBind( @NonNull RecyclerView mRecyclerViewPets, @NonNull AppCompatActivity activity,
                                            @NonNull RescuePetsViewModel rescuePetsViewModel ) {
        LiveData< List< Pet > > petLiveData = rescuePetsViewModel.getAllPets();
        if ( petLiveData == null )
            return;
        petLiveData.observe( activity, new Observer< List< Pet > >() {
            @Override
            public void onChanged( @Nullable final List< Pet > pets ) {
                if ( pets == null )
                    return;
                PetAdapter petAdapter = ( PetAdapter ) mRecyclerViewPets.getAdapter();
                Objects.requireNonNull( petAdapter ).setPets( pets );
            }
        } );
    }

    @BindingAdapter( { "recycleViewFormsBind", "activity", "userUid" } )
    public static void recycleViewFormsBind( @NonNull RecyclerView mRecyclerView, @NonNull AppCompatActivity activity,
                                             @NonNull RescuePetsViewModel rescuePetsViewModel, @NonNull String userUid ) {

        LiveData< User > userLiveData = rescuePetsViewModel.getUser( userUid );
        if ( userLiveData == null ) {
            Timber.wtf( "user live data is null" );
            return;
        }
        userLiveData.observe( activity, new Observer< User >() {
            @Override
            public void onChanged( User user ) {
                if ( user == null )
                    return;
                if ( user.getCenterUid() != null && !user.getCenterUid().isEmpty() ) {//employee
                    rescuePetsViewModel.getAllAdoptionForms().observe( activity, new Observer< List< AdoptionForm > >() {
                        @Override
                        public void onChanged( @Nullable final List< AdoptionForm > adoptionForms ) {
                            AdoptionFormAdapter adapter = ( AdoptionFormAdapter ) mRecyclerView.getAdapter();
                            Objects.requireNonNull( adapter ).setAdoptionForms( adoptionForms );
                        }
                    } );
                } else {
                    rescuePetsViewModel.getAdoptionFormsByUserUid( userUid ).observe( activity, new Observer< List< AdoptionForm > >() {
                        @Override
                        public void onChanged( @Nullable final List< AdoptionForm > adoptionForms ) {
                            AdoptionFormAdapter adapter = ( AdoptionFormAdapter ) mRecyclerView.getAdapter();
                            Objects.requireNonNull( adapter ).setAdoptionForms( adoptionForms );
                        }
                    } );
                }
            }
        } );

    }

    @BindingAdapter( { "petProfileBind", "PetUid", "context" } )
    public static void petProfileBind( @NonNull ActivityPetProfileBinding binding,
                                       @NonNull View view,
                                       @NonNull AppCompatActivity activity,
                                       @NonNull RescuePetsViewModel rescuePetsViewModel,
                                       @NonNull String petUid ) {
        LiveData< Pet > petLiveData = rescuePetsViewModel.getPet( petUid );
        if ( petLiveData == null )
            return;
        petLiveData.observe( activity, new Observer< Pet >() {
            @Override
            public void onChanged( Pet pet ) {
                if ( pet == null )
                    return;
                binding.nameUpPet.setText( pet.getName() );
                binding.nameEtPet.setText( pet.getName() );
                binding.speciesEtPet.setText( pet.getSpecies() );
                binding.breedEtPet.setText( pet.getBreed() );
                binding.birthDateEt.setText( String.valueOf( pet.getBirthYear() ) );
                binding.descriptionEtPet.setText( pet.getDescription() );
                try {
                    Glide.with( view ).load( pet.getProfileImage() )
                            .placeholder( R.drawable.profile_pic )
                            .into( ( ImageView ) binding.profilePic );
                } catch ( Exception e ) {
                    Timber.e( e );
                }

                // Handle click, logOut
                binding.AdoptBtn.setOnClickListener( view -> {
                    Intent intent = new Intent( activity, PetAdoptActivity.class );
                    Bundle bundle = new Bundle();
                    bundle.putSerializable( "pet", pet );
                    intent.putExtras( bundle );
                    activity.startActivity( intent );
                } );

                // handle click, open edit profile
                binding.profileEditBtnPet.setOnClickListener( view -> {
                    Intent intent = new Intent( activity, PetProfileEditActivity.class );
                    Bundle bundle = new Bundle();
                    bundle.putSerializable( "pet", pet );
                    intent.putExtras( bundle );
                    activity.startActivity( intent );
                } );

            }
        } );
    }

    @BindingAdapter( { "centerProfileBind", "PetUid", "context" } )
    public static void centerProfileBind( @NonNull ActivityCenterInfoBinding binding,
                                          @NonNull View view,
                                          @NonNull AppCompatActivity activity,
                                          @NonNull RescuePetsViewModel rescuePetsViewModel,
                                          @NonNull String petUid ) {
        LiveData< Center > centerLiveData = rescuePetsViewModel.getCenter( petUid );
        if ( centerLiveData == null )
            return;
        centerLiveData.observe( activity, new Observer< Center >() {
            @Override
            public void onChanged( Center center ) {
                if ( center == null )
                    return;

                LiveData< Address > addressLiveData = rescuePetsViewModel.getAddress( center.getAddressUid() );
                if ( addressLiveData != null ) {
                    addressLiveData.observe( activity, new Observer< Address >() {
                        @Override
                        public void onChanged( Address address ) {
                            if ( address == null )
                                return;
                            binding.county.setText( address.getCounty() );
                            binding.city.setText( address.getCity() );
                            binding.street.setText( address.getStreet() );
                            binding.postalCode.setText( address.getPostalCode() );
                        }
                    } );
                }

                binding.phoneNumber.setText( center.getPhoneNumber() );
                binding.schedule.setText( center.getSchedule() );

                LiveData< List< BankInfo > > bankInfosLiveData = rescuePetsViewModel.getBankInfoByCenterUid( center.getUid() );
                if ( bankInfosLiveData != null ) {
                    bankInfosLiveData.observe( activity, new Observer< List< BankInfo > >() {
                        @Override
                        public void onChanged( List< BankInfo > bankInfos ) {
                            if ( bankInfos == null || bankInfos.isEmpty() )
                                return;
                            BankInfo bankInfo = bankInfos.get( 0 );
                            if ( bankInfo == null )
                                return;
                            binding.iban.setText( bankInfo.getIban() );
                        }
                    } );
                }
            }
        } );
    }


    @BindingAdapter( { "adoptionFormProfileBind", "PetUid", "context" } )
    public static void adoptionFormProfileBind( @NonNull ActivityAdoptionFormProfileBinding binding,
                                                @NonNull View view,
                                                @NonNull AppCompatActivity activity,
                                                @NonNull RescuePetsViewModel rescuePetsViewModel,
                                                @NonNull String adoptionFormUid ) {

        LiveData< AdoptionForm > adoptionFormLiveData = rescuePetsViewModel.getAdoptionForm( adoptionFormUid );
        if ( adoptionFormLiveData == null )
            return;
        adoptionFormLiveData.observe( activity, new Observer< AdoptionForm >() {
            @Override
            public void onChanged( AdoptionForm adoptionForm ) {
                if ( adoptionForm == null )
                    return;

                binding.nameEtPet.setText( adoptionForm.getNickName() );
                binding.email.setText( adoptionForm.getContactEmail() );
                binding.phone.setText( adoptionForm.getContactPhone() );
                rescuePetsViewModel.getPet( adoptionForm.getPetUid() ).observe( activity, new Observer< Pet >() {
                    @Override
                    public void onChanged( Pet pet ) {
                        if ( pet == null )
                            return;
                        binding.nameUpPet.setText( pet.getName() );
                        binding.speciesEtPet.setText( pet.getSpecies() );
                        binding.breedEtPet.setText( pet.getBreed() );
                        binding.birthDateEt.setText( pet.getBirthYear() );
                        binding.descriptionEtPet.setText( pet.getDescription() );
                        try {
                            Glide.with( view ).load( pet.getProfileImage() )
                                    .placeholder( R.drawable.profile_pic )
                                    .into( ( ImageView ) binding.profilePic );
                        } catch ( Exception e ) {
                            Timber.e( e );
                        }
                    }

                } );

                binding.desiredDate.setText( adoptionForm.getDate() );
                binding.formComment.setText( adoptionForm.getComment() );
                Boolean status = adoptionForm.getStatus();
                if ( status == null )
                    binding.status.setText( R.string.pending );
                else if ( status )
                    binding.status.setText( R.string.approved );
                else
                    binding.status.setText( R.string.denied );

                if ( adoptionForm.getEmployeeUid() == null ) {

                    binding.employeeLine.setVisibility( View.GONE );
                } else {
                    LiveData< Employee > employeeLiveData = rescuePetsViewModel.getEmployee( adoptionForm.getEmployeeUid() );
                    if ( employeeLiveData != null ) {
                        employeeLiveData.observe( activity, new Observer< Employee >() {
                            @Override
                            public void onChanged( Employee employee ) {
                                if ( employee == null || employee.getName().isEmpty() )
                                    binding.employeeLine.setVisibility( View.GONE );
                                else {
                                    binding.employeeLine.setVisibility( View.VISIBLE );
                                    binding.employeeName.setText( employee.getName() );
                                }
                            }
                        } );
                    }
                }

            }
        } );
    }

}
