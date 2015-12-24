# IsThatEdible
Created and designed completely on my own it is essentially an Android app to monitor food expirations.

So far the app supports the following:
* Add food items with an expiration date
* Receive notification for expired items when near a store
* Receive notification when an item expires
* Create custom locations to use with notification for an expired item (i.e. favorite grocery store)
* View detailed information about a food item (i.e. expiration, how it's stored, etc.)
* Landscape and portrait mode

It was originally created for my semester project for Advanced Mobile Development and so tries to utilize many different features of Android including:
* Fragments
* Background service used with Google Geofences
* Broadcast receiver which receives geofence updates and handles any necessary actions
* Voice commands to search for food items
* SQLite database for persistant storage of food items and locations
* Android Settings activity

