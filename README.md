# AppSRD #

The project is based on Machine Learning.
<br> The diagnosis in the application is made by using a CNN model for image prediction which is a Deep Learning algorithm, when the model is placed on a server and the application is a platform/ user interface that communicates with the server
### Motivation ###
A significant part of the population suffers from skin rashes.
The queues for dermatologists are long (the average wait is about 20 days) and leave the person in uncertainty and discomfort.
### Our product ###
SRD detects rashes and skin lesions caused by various reasons such as allergies, infectious diseases, and more.
<br>The application gives a quick and accurate diagnosis and allays or verifies concern by showing the type of rash and its severity. It gives a rough estimate and is not a substitute for medical advice.

### WORKFLOW ###
SRD works like this: 
<br> First, the user logs in to the application - only registered users can use it. 
<br> The application allows the user to take a picture or upload a picture from the gallery and when he chooses a picture the application sends it to the server,
<br>the server receives it and activates our model the model makes a prediction for the picture and returns the classification to the application,
<br> and the application shows the user the type of rash detected and if it could be life-threatening or there is no concern and in addition there is a link for more information.

### DataSet ###
We created a DataSet of five types of rashes by using images from certified dermatology websites that approve the use of images.
<br>The 5 types of rashes we have chosen are rashes that are common in Israel, 3 of them indicate a potentially life-threatening situation and 2 of them do not.
<br>We divided the pictures into 2:
* The Train group - we will train the model with these pictures, and they are 80% percent of the total pictures,
<br>When 90% of the images are used for training and 10% of the images are used for verification and improvement of the model.
* The Test group which is used to test the model and evaluate it - what is its accuracy percentage and what is its error percentage. 
<br>And it is 20% of the total number of images.
