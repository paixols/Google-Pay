<<<<<<< HEAD
**Edit a file, create a new file, and clone from Bitbucket in under 2 minutes**

When you're done, you can delete the content in this README and update the file with details for others getting started with your repository.

*We recommend that you open this README in another tab as you perform the tasks below. You can [watch our video](https://youtu.be/0ocf7u76WSo) for a full demo of all the steps in this tutorial. Open the video in a new tab to avoid leaving Bitbucket.*

---

## Edit a file

You’ll start by editing this README file to learn how to edit a file in Bitbucket.

1. Click **Source** on the left side.
2. Click the README.md link from the list of files.
3. Click the **Edit** button.
4. Delete the following text: *Delete this line to make a change to the README from Bitbucket.*
5. After making your change, click **Commit** and then **Commit** again in the dialog. The commit page will open and you’ll see the change you just made.
6. Go back to the **Source** page.

---

## Create a file

Next, you’ll add a new file to this repository.

1. Click the **New file** button at the top of the **Source** page.
2. Give the file a filename of **contributors.txt**.
3. Enter your name in the empty file space.
4. Click **Commit** and then **Commit** again in the dialog.
5. Go back to the **Source** page.

Before you move on, go ahead and explore the repository. You've already seen the **Source** page, but check out the **Commits**, **Branches**, and **Settings** pages.

---

## Clone a repository

Use these steps to clone from SourceTree, our client for using the repository command-line free. Cloning allows you to work on your files locally. If you don't yet have SourceTree, [download and install first](https://www.sourcetreeapp.com/). If you prefer to clone from the command line, see [Clone a repository](https://confluence.atlassian.com/x/4whODQ).

1. You’ll see the clone button under the **Source** heading. Click that button.
2. Now click **Check out in SourceTree**. You may need to create a SourceTree account or log in.
3. When you see the **Clone New** dialog in SourceTree, update the destination path and name if you’d like to and then click **Clone**.
4. Open the directory you just created to see your repository’s files.

Now that you're more familiar with your Bitbucket repository, go ahead and add a new file locally. You can [push your change back to Bitbucket with SourceTree](https://confluence.atlassian.com/x/iqyBMg), or you can [add, commit,](https://confluence.atlassian.com/x/8QhODQ) and [push from the command line](https://confluence.atlassian.com/x/NQ0zDQ).
=======
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

>>>>>>> 4f633e7f075b90e350f43a93c09b29a807c923f2
