# Technical Design Of MapChat

We used Firebase Authentification to allow users to sign in and make profiles.
Using Realtime Databases, we were able to post messages, store user messages and locations, and display them in textviews.
Using Firebase Storage, we stored user profile pictures and were able to display them on their respective user profiles.
We utilise the users gallery to allow them to pick out a profile picture.
We asked users to allow us permission to get their location so we could use the map function.
We used a Google Maps Activity and Fragment to allow users to look at the map and move around on it.
For out own benefit, we utilised Firebase Crashyltics and connected it to Jira, giving us nice info on all user crashes.

If we were to start the project from scratch again, we would change from the default authentification system to one that we could customize better.
The default looks bvery out of place in our colourful app, and by the time we looked into changing it, too much relied on it.
For aesthetic reasons, we would have liked to have made the ui look better, with better boxes for posts, and more seamless integrated buttons.
I would also like if maybe the map opened first rather than the reading page, but that is something small.


