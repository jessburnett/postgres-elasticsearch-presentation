This covers the basic information required to write this presentation:

A structure for the presentation
--------------------------------

http://speaking.io/

###  The idea

A comparison of GIN, GiST and the core algorithm that elastic search uses.
A way to use elastic search from postgres.

### Outlining

The talk is two sections so there should be two separate outlines.

Introduction to the talk

#### Algorithms

Searching text
 * Direct comparison and LIKE ILIKE don't cover most use cases
 * Contains, near and fuzzy matching
 * Scoring instead of binary match / no-match

GIN Algorithm
 * Generalized Inverted Index
 * Postgres
 * Performance characteristics

GiST Algorithm
 * Generalized Search Tree
 * Postgres
 * Performance characteristics

ES Algorithm
 * lucene
 * http://stackoverflow.com/questions/2602253/how-does-lucene-index-documents#2607757

Complexity Comparison
 * Space Memory CPU
 * Big-O

Outro/Intro of most-efficient-algorithm breakpoints
 * Show concrete numbers or graph

#### ES FDW

Intro to using elastic search
 * Indicate documentation, assume running instance with some loaded documents
 * Can have a demo project which sets up an es instance
 * Show how to query using curl

Using elastic search from postgres
 * Intro to foreign data wrappers
 * Set up for es fdw
 * Triggering the fdw
 * Using the fdw as an index

Scripting to reduce excess data returned
 * Groovy
 * Only return the id of the matching document (and score?)

Caveats
 * Difference in transactions, can be hard to roll back

Comparison of actual search speeds
 * Table or graph

Outro to the talk
 * Github project
 * Previous talk on fdw



### Repetition

There is a lot of content and there needs to be a common repeated theme
throughout this talk.

 * Text searches are rarely exact matches?
 * Different algorithms have very different performance characteristics
 * Knowing the application indicates what trade offs are acceptable

I may want to revisit the above based on those thoughts.

### How many slides?

Practice the talk and find out.

Probably a lot to cover all the content.

### Entertainment

Try to make the talk engaging.

Would be good to keep the word count low and to use gifs or similar to convey complex topics.

### Color

Need to pick a palette from http://www.colourlovers.com

Need to do section slides in a different color

Can do the last few slides in a different color to remind myself that I am coming to the end

### Typography

 * You can’t go too big
 * Use a ton of contrast
 * Don’t use many words
 * Choose a solid font
