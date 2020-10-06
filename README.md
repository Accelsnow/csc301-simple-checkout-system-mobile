CSC301 - A1 - CHECKOUT APP - MADE BY Youhai Li & Junan Zhao

# Mobile App APK
Mobile App: [https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile/blob/master/app/release/app-release.apk](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile/blob/master/app/release/app-release.apk)

# Instructions

Links for our APP&#39;s repos:

Backend: [https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-backend](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-backend)

MOB Frontend: [https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile)

**Design Overview**

- We decide to implement a checkout system backed up by an item inventory supported by MySQL database. Any user from WEB end or MOBILE end of our APP can search to add item, modify cart, and see the calculated total of the items (involving individual item discount, global cart discount, and global tax rate as parameter), and each session ends after the user confirms the receipt. However, considering that manager operations on the database (editing certain fields of item, removing item, adding item under restriction, and editing global taxes &amp; discounts) are sophisticated and table with massive information are not visually clean and tidy on MOBILE end, we decide to implement the manager control portal only on the WEB end.

**Functionality Instruction**

- User can type in the correct item id (if the user knows the target item&#39;s id) or standard item name (e.g. coke, sprite…) to add that item (quantity 1) into the cart. If the item is already in the cart, increment that item&#39;s quantity by 1. Error messages will be given if such item does not exist or is currently out of stock.

- User can see item information of all items in the cart.

- User can edit the quantity of each item in the cart by manually typing in the input field or by clicking the +1 -1 buttons. The +1 and -1 buttons limits the number range of the item in \[1,item stock], while the manual input does not check quantity validity at the time of input. However, at the time of checkout, if any item has a quantity more than its stock, a warning will show up and the quantity will be automatically set the maximum amount allowed (=stock number). The user can use the remove button to remove an item from the cart.

- User can click the refresh button to update all items' information in the server to get their latest information updated (stock, price, etc.).

- User can click the checkout button to see the receipt which includes more detailed information (total discount, tax rate, final total price). If the user clicks the confirm button, a purchase is completed and the cart will be cleaned and inventory will be updated correctly (thus entering next purchase), else user is sent back to the cart (the purchase remains pending).

- If the quntity of an item drops below the user's desired purchase amount at the time of checkout (maybe some other user purchased it first, since the cart does not have real-time updated stock), a warning will be displayed, notifying the user that some items in the cart were unable to be purchased. However, all items that are eligible for purchasing will be purchased. After the checkout completes, if any item had such an error, those items will remain in cart with their latest stock updated (or removed if they no longer exist), while all items successfully purchased will be removed from the cart.


**Frontend Interface Design**

- Main page (cart):
  - An item search bar to add items into the cart
  - A card view list of items in the cart with their information
  - A checkout button and a refresh button

 - Checkout dialog:
  - Display a brief of all items currently in cart ready to be purchased (item name, quantity, total price).
  - The current tax rate and overall discount rate.
  - The sum total price after applying tax and discounts.
  - A "checkout" button to confirm the purchase, and a cancel button to return back to the cart.
  
  
**Testing Instruction**

- Testing for App functionality

The release APK is located at app/release/app-release.apk . It is signed and ready to be installed on android devices or simulators (optimized for android 8.0+ API level 26+). However, since the application is not verified by google play store(since we did not acquire a developer license), it is mandatory to turn on "allow install from unknown source" option in android settings. There will also be a play store warning showing the application is not formally verified by google play, which can also be supressed. There is a video located under the root of the directory named [demostration.mp4](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile/blob/master/demonstration.mp4) showing how to install and use the application.

Alternatively, you may download/clone the source code and open it with android studio and run it on any android device/simulator. The app testing during development is primarily done through android-studio by pushing the application through adb to my own Pixel 4XL device.

**Backend Models**

- The database models we implement:

Manager: {

username: String, // username

password\_hash: String // passoword

}

Item: {

id: Integer, // unique item id

name: String, // item name

discount: Float, // item discount rate should be in range [0,1]

price: Float, // item price should be in range [0,

stock: Integer // iten stock should be in range [0,

}

Checkout: {

id: Integer, // unique checkout id

tax\_rate: Float, // global tax rate should be in range [0,

discount: Float // global discount should be in range [0,1]

}
