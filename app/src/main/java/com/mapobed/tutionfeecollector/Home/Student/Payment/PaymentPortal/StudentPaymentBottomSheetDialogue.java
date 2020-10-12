package com.mapobed.tutionfeecollector.Home.Student.Payment.PaymentPortal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapobed.tutionfeecollector.R;

public class StudentPaymentBottomSheetDialogue extends BottomSheetDialogFragment {
    private BottomSheetButtonListener bottomSheetButtonListener;

    @Nullable
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetButtonListener = (BottomSheetButtonListener) context;
        } catch (ClassCastException c) {
            throw new ClassCastException(context.toString() + "Bottom Sheet Listener must be implemented.");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upi_payment_bottom_sheet_layout, container, false);
         ImageView paytm = v.findViewById(R.id.pay_tm);
        ImageView phonepay = v.findViewById(R.id.phone_pay);
        ImageView gpay = v.findViewById(R.id.g_pay);
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetButtonListener.buttonClicked("Paytm");
                dismiss();

            }
        });
        phonepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetButtonListener.buttonClicked("PhonePay");
                dismiss();

            }
        });
        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetButtonListener.buttonClicked("GPay");
                dismiss();

            }
        });
        return v;
    }

    public interface BottomSheetButtonListener {
        void buttonClicked(String text);
    }


}
