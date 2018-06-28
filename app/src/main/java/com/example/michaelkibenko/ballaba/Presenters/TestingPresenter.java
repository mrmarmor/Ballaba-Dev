package com.example.michaelkibenko.ballaba.Presenters;

import android.content.Context;
import android.content.Intent;

import com.example.michaelkibenko.ballaba.Activities.AddPropertyActivity;
import com.example.michaelkibenko.ballaba.Activities.CreditCardActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterCodeActivity;
import com.example.michaelkibenko.ballaba.Activities.EnterPhoneNumberActivity;
import com.example.michaelkibenko.ballaba.Activities.GuaranteeAgreementActivity;
import com.example.michaelkibenko.ballaba.Activities.Guarantor.GuaranteeRequestActivity;
import com.example.michaelkibenko.ballaba.Activities.Guarantor.GuarantorDeclinedActivity;
import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Activities.PreviewAgreementActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyDescriptionActivity;
import com.example.michaelkibenko.ballaba.Activities.PropertyManagementActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringWelcomeActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.SelfCamActivity;
import com.example.michaelkibenko.ballaba.Activities.SplashActivity;

/**
 * Created by michaelkibenko on 21/02/2018.
 */

public class TestingPresenter extends BasePresenter {

    private Context context;
    private Intent enterPhoneNumberIntent, enterCodeIntent;


    public TestingPresenter(Context context){
        this.context = context;
        enterPhoneNumberIntent = new Intent(this.context, EnterPhoneNumberActivity.class);
        enterCodeIntent = new Intent(this.context, EnterCodeActivity.class);
    }

    public void gotoEnterCodeIntent(){
        context.startActivity(enterCodeIntent);
    }

    public void gotoEnterPhoneNumberIntent(){
        context.startActivity(enterPhoneNumberIntent);
    }

    public void onSearchClicked(){
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public void onFlowStartedClicked(){
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    public void onScoringClicked(){
        context.startActivity(new Intent(context, ScoringWelcomeActivity.class));
    }

    public void onAddPropertyClicked(){
        context.startActivity(new Intent(context, AddPropertyActivity.class));
    }

    public void onMyPropertyClicked(){
        context.startActivity(new Intent(context, PropertyManagementActivity.class));
    }

    public void goToCalendar(){
        context.startActivity(new Intent(context, PreviewAgreementActivity.class));
    }

    public void goToselfiCam(){
        context.startActivity(new Intent(context, SelfCamActivity.class));
    }

    public void goToGuaranteeDeclined(){
        context.startActivity(new Intent(context, GuarantorDeclinedActivity.class));
    }

    public void goToGuarantor(){
        context.startActivity(new Intent(context, GuaranteeRequestActivity.class));
    }

    public void goToCreditCard(){
        context.startActivity(new Intent(context, CreditCardActivity.class));
    }

    public void goToPropertyDescription(){
        Intent intent = new Intent(context, PropertyDescriptionActivity.class);
        //intent.putExtra(PropertyDescriptionActivity.PROPERTY , "87");
        context.startActivity(intent);
    }

    public void goToGuaranteeAgreement(){
        context.startActivity(new Intent(context, GuaranteeAgreementActivity.class));
    }
}
