<h1>Simple searchengine
</h1>
<h3>Introduction</h3>
simple implementation of com.findwise searchengine. Due to in memory 
implementation the efficiency is not high. Added springboot dependency 
in order to provide web container and simple external access.

Simple engine provide one phrase word searching. 
Document indexing begins of single phrase preparation:
- divide words in document
- alphabetic sort
- map to lowercase
- remove punctuation
- remove blank position


<div>
Phrases which ends with punctuation (e.g "," or ".") are indexed without them, single punctuation are also not stored.

To prepare suitable with assignment requirements response InMemoryIndex has map form where:
- key = word
- value = Set\<IndexEntry\> (set used to differentiate all document's score)


</div>

<h3>available endpoints</h3>
<div><b>
GET /search/{term}</b>  feature to find inverted index of searching phrase. Term is a path variable.
</div>
<div><b>
POST /search</b>  feature to index attached document. Required 
Simply engine provide one phase word in body.
</div>


<div>

<h3>Known issues & TODO:</h3>

<li> Implement rest of assigmnment goals (implement TF-IDF sorting)
<li> With intensified POST requests InMemoryIndex throws null in some lambda function.
<li> Add slf4j logger
<li> test entire SimpleEngine



</div>