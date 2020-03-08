![Expense Tracker](https://github.com/shubham99bisht/ExpenseTracker/blob/master/logo_black.png)
----------------------------------

# Expense Tracker
An Android app that helps store images of their invoices and details fetched from Machine Learning Model in Database.

### Features:
* The app allows user to take pictures of their images and sends it to the server in the backend.
* **_It runs the Tesseract OCR over the image and gives the output in the textfile._**
* **_After getting the textfile, it extracts the necessary details such as Vendor Name, Date, Amount, Address, Invoice ID etc from the textfile and sends it back to the app._**
* For extracting details, **Bi-Direction Long Short-Term Memory (LSTM)** model is used on the server.
* It also allows users to see past bills and stores their images in **Firebase Database.**
* User can also view the expenses in form of __Piechart__.

#### ScreenShots:
Screenshots | Screenshots | Screenshots
:----------------:|:----------------:|:----------------:
![Login](https://github.com/shubham99bisht/ExpenseTracker/blob/master/Screenshots/Login.jpeg) | ![Signup](https://github.com/shubham99bisht/ExpenseTracker/blob/master/Screenshots/Signup.jpeg) | ![Bills](https://github.com/shubham99bisht/ExpenseTracker/blob/master/Screenshots/Bills.jpeg)
![Camera](https://github.com/shubham99bisht/ExpenseTracker/blob/master/Screenshots/camera.jpeg) | ![Verification](https://github.com/shubham99bisht/ExpenseTracker/blob/master/Screenshots/verification.jpeg) | ![Graph](https://github.com/shubham99bisht/ExpenseTracker/blob/master/Screenshots/graph.jpeg)
