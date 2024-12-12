import pandas as pd
from flask import Flask, request, jsonify
from pandas import DataFrame
from analytics import getAnalysisData


app = Flask(__name__)

    
@app.post("/analytics")
def analitica():
    data = request.get_json(True)
    return getAnalysisData(data)



