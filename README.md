# Google-Pay for Android
Google Pay integration developed in Kotlin at Google San Francisco Launchathon on 06/04/2018

# Firebase Integration
> Create a Firebase project & Integrate it to handle payments with Stripe using Firebase Cloud Functions

- Donwload or clone this repo.
- Deploy a Firebase project from your terminal using the google cloud functions found [here] .
- Enable Firebase e-mail authentication for your Firebase project.
- Upgrade to Firebase project to Flame or Blaze plan to be able to work with Google Cloud Functions.
- Add your google-services.json file to your Android project.
```
goole-services.json
```
- Create a Stripe Account to get your API keys.
- Add your Stripe publishable key to the Constants.kt file found under /Utilities directory.
```
object StripeTokens {
    /*Test Stripe Account Token*/
    const val publishableKey = "****ADD YOUR STRIPE TEST PUBLISHABLE KEY***"
}
```
- Run & Test using the Stripe Widget or Google Pay payment integrations!

# Android App
- Firebase Auth for Log in, Sign Up, Sign Out & Forotten password.
- Firebase Realtime Database used to handle last payment added and payment information.
- Firebase Cloud Functions used to handle Stripe authentication, token retrieval and payment transactions.

# References
- [Firebase Cloud Functions] https://firebase.google.com/docs/functions/
- [Process Payments with Firebase] https://firebase.google.com/docs/use-cases/payments
- [FireStripe Example] https://github.com/firebase/functions-samples/tree/master/stripe
- [Stripe Android SDK] https://stripe.com/docs/mobile/android

# Contact
> Let me know if I can help by contacting me through my [website] https://paixols.com/

