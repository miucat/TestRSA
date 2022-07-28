package ro.anagrama.testrsa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import ro.anagrama.testrsa.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvSuccessCounter.setText("0");
        binding.tvErrCounter.setText("0");
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void setTextAfterParse(int error, int success, String code) {
        binding.tvSuccessCounter.setText(String.valueOf(success));
        binding.tvErrCounter.setText(String.valueOf(error));
        if (code != null)
            binding.codeDecompilat.setText(code);
        else
            binding.codeDecompilat.setText("o mare eroare");

    }

    public void setPem( String pem) {
        binding.pemKey.setText(pem);
    }

}