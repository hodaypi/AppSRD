# Local server for running the machine

import os
from msilib.schema import File

import pyrebase

from flask import Flask, request, jsonify, render_template
import pickle
import numpy as np
from tensorflow.keras.preprocessing import image
import tensorflow as tf

# pyrebase
# firebase


config = {
    "apiKey": "AIzaSyC9grKJV8n4FnnO5SZNaSHSD0NAXqSonUc",
    "authDomain": "srdappai.firebaseapp.com",
    "projectId": "srdappai",
    "storageBucket": "srdappai.appspot.com",
    "messagingSenderId": "61889271986",
    "appId": "1:242546051756:android:4dea49fe9994ee456fad09",
    "measurementId": "G-4B6SL5LB6P",
    "databaseURL": "https://srdappai.firebaseio.com"

}

# user='cpHHu8He2iefXGxVFsFRrpG3rhp2'
# path_on_cloud='images/'+ user +'/'+'uy.jpg'
# # path_local="tryPso.jpg"
# # storage.child(path_on_cloud).put(path_local)
# storage.child(path_on_cloud).download("aaa.jpg")


app = Flask(__name__)

MODEL_PATH = r"C:\Users\hoday\Documents\GitHub\pythonProject\inceptionV3AdamSoftmax.weights.best.h5"
model = tf.keras.models.load_model(MODEL_PATH)


@app.route('/', methods=['GET', 'POST'])
def helloPost():
    if request.method == 'POST':
        # value = request.form['url']
        user = request.form['id']
        # user='cpHHu8He2iefXGxVFsFRrpG3rhp2'

        firebase = pyrebase.initialize_app(config)
        storage = firebase.storage()
        path_on_cloud = 'images/' + user + '/' + 'pre.jpeg'
        print(path_on_cloud)
        # path_local="ds.jpg"
        # storage.child(path_on_cloud).put(path_local)
        # path_on_cloud='images/'+ user +'/'+'new.jpg'
        storage.child(path_on_cloud).download(filename="a1.jpeg")

        # File localFile = File.createTempFile("images", "jpg");

        # MODEL_PATH = r"C:\Users\student\Documents\GitHub\SRD\inceptionV3AdamSoftmax.weights.best.h5"
        # IMG_PATH = r"C:\Users\hoday\Documents\GitHub\server\try.jpg"
        # IMG_PATH=str(value)
        # model = tf.keras.models.load_model(MODEL_PATH)
        IMG_PATH = r"a1.jpeg"
        img = image.load_img(IMG_PATH, target_size=(400, 400))
        # img = img.save("a2.jpeg")
        x = image.img_to_array(img)
        x = np.expand_dims(x, axis=0)

        test_image = np.vstack([x])
        classes = model.predict(test_image)
        predictions = []
        for i in range(0, 5):
            predictions.append(classes[0][i])
        # print(predictions)

        max = 0
        for i in range(0, 5):
            print(predictions[i])
            if predictions[i] > max:
                max = predictions[i]
                rush = i

        skinRushes = ['AtopicDermatitis', 'lupus', 'PityriasisRosea', 'Psoriasis', 'Urticaria', "No"]

        print("max   ", max)

        if (max <= 0.7):
            print("no find")
            return skinRushes[5]

        print(skinRushes[rush])
        # value = request.form['url']
        # print(value)
        # va="AtopicDermatitis"
        # return skinRushes[i]

        # bucket = admin_storage.bucket()
        # blob = bucket.blob('images/temp/pumpkin.jpg')
        # print(blob)
        # blob.delete()

        return skinRushes[rush]
    else:
        return "bla"


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
