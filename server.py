import flask
from flask import Flask, render_template, request, send_from_directory, redirect, url_for
from werkzeug.utils import secure_filename
import os, time
app = Flask(__name__)

#-------------------------------------------------------------------------------------------
@app.route("/", methods=['GET','POST'])
@app.route("/index")
def index():
    return "Home Page - Flask running successfully"
#    return render_template("index.html")

#-------------------------------------------------------------------------------------------


@app.route("/invoice_upload", methods=["POST"])
def invoice_upload():
    if request.method == "POST":
        f= request.files["image"]
        filename = secure_filename(f.filename)
        basedir = os.path.abspath(os.path.dirname(__file__))
        id = "i" + str(int(time.time()))
        save_name = id + ".png"
        f.save(os.path.join(basedir, "uploads", save_name))
        #return render_template("index.html")
        return "file uploaded"

@app.route("/random", methods=['GET','POST'])
def check():
    print(request.data)
    return "Got link"

app.run(host='0.0.0.0', port=5000, debug=True)
