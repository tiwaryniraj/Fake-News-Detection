# Fake-News-Detection

## Why we need this project?

We know that this problem is spreading fast and needs to be Limited as New York time has stated *"As fake news bread slice more readers shrug at the truth"*.
Many research paper has been published in this area,As the readers come across many fake news when they read a real news they believe that it is also a fake news.
well detecting a fake news is not an easy task as that can be many definitions of a fake news, but is some extent it is possible using some machine learning models we will also be needing to use NLP techniques to make computer understand the news as there is a waste material of text involved.

## Dependencies
Pandas, Numpy, NLTK, Matplotlib, Sklearn, word2vec-GoogleNews-vectors, TSNE for high visualization better than PCA, Snowball Stemming, Gensim.

## Overview
We use Natural Language Processing to rectify the problem, We got a data set of 6335 news articles which is labelled as fake or real so we use 30% of the data to train the model and rest of 70% data to test the model, With this our project concluded with 92% of the accuracy.

## Project model explanation,
We have use the csv file to read the data, then we applied the text preprocessing techniques as Stemming, Stop-word removal and lemmatization.

Techniques are as follows:-
1) Begin by removing the html tags.
2) Remove any punctuations or limited set of special characters like , or . or # etc.
3) Check if the word is made up of english letters and is not alpha-numeric
4) Check to see if the length of the word is greater than 2 (as it was researched that there is no adjective in 2-letters)
5) Convert the word to lowercase
6) Remove Stopwords
7) Finally Snowball Stemming the word (it was obsereved to be better than Porter Stemming)
8) After which we collect the words used to describe fake and real news.

Then we had used google pretrained model "GoogleNews-vectors-negative300" for more similar words and applied high dimensionality reduction technique TSNE(t-distributed stochastic neighbor embedding).

Then next, we converting word to vector, technique such as Average Word2Vector.

And after completing word2vector we applied classification techniques such as K Nearest Neighbor with K-fold validation, Naive Bayes, Logistic Regression and Support Vector Machine.

From which Logistic Regression gives the best accuracy of 92%.
In logistic Rgression we use gridsearch and randomsearch for optimal C in L2 and L1 Regularization.

![Image](https://image.ibb.co/mp4pmo/img.png)

## Future
We are hoping to Create a website, which will have our machine learning model as a backend and user will be able to paste any news article which he/she thinks is not legitimate.
Our website will take that article and classify it as fake or real so that the user can be able to make decision of believing it or not.
