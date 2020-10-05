CSC301 - A1 - CHECKOUT APP - MADE BY Youhai Li & Junan Zhao
# Mobile App APK
Mobile App: [https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile/blob/master/app/release/app-release.apk](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile/blob/master/app/release/app-release.apk)
# Instructions

Links for our APP&#39;s repos:

Backend: [https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-backend](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-backend)

MOB Frontend:[https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile](https://github.com/Accelsnow/assignment-1-4-accelsnow-altair59-mobile)

**Design Overview**

- We decide to implement a checkout system backed up by an item inventory supported by MySQL database. Any user from WEB end or MOBILE end of our APP can search to add item, modify cart, and see the calculated total of the items (involving individual item discount, global cart discount, and global tax rate as parameter), and each session ends after the user confirms the receipt. However, considering that manager operations on the inventory (editing certain fields of item, removing item, adding item under restriction, and editing global taxes &amp; discounts) are sophisticated to be performed and table with massive information are not visually clean and tidy on MOBILE end, we decide to implement access (of course with authorization) to manager portal only on WEB end.

**Functionality Instruction**

- User can type in the correct item id (if the user knows the target item&#39;s id) or standard item name (e.g. coke, sprite…) to add that item (quantity 1) into the cart. If the item is already in the cart, increment that item&#39;s quantity by 1. Error messages will be given if such item does not exist or is currently out of stock.

- User can see proper information of all items in the cart.

- User can edit the quantity of each item in the cart manually in the range of [0, stock]. Item will be removed from the cart if invalid input is given.

  REQUIREMENT:
  0 <= quantity <= stock

- User can click the checkout button to see the receipt which includes more detailed information (cart discount, cart tax rate, total, and time of checkout). If the user clicks the confirm button, a purchase is completed and the cart will be cleaned and inventory will be updated correctly (thus entering next purchase), else user is sent back to the cart (the purchase remains pending).

- If someone else also bought this item before the user checks out, the quantity of that item in the cart will be adjusted if needed (e.g. if A wants to buy 2 cokes but the stock of coke is changed from 2 to 1 since B buys 1 coke before A checks out, then when checks out A will be notified of this change and coke quantity in A&#39;s cart will be updated to 1 accordingly).

**Frontend Component Design**

- Checkout page:
  - An item search bar to add items into the cart
  - A table of items in the cart with proper information
  - A calculator of total cost of items in the cart
  - A checkout button
  - A modal displaying receipt information
  - A log in button to enter manager session
  
**Backend Model Design**

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

**Testing Instruction**

- Testing for App functionality

1. Please refer to section Functionality Instruction for all operations and behaviours.
