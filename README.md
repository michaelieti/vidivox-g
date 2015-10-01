=============================
# VIDIVOX PLANNING - 01.02 #
=============================

## FEEDBACK ##
Feedback from Nasser, Oct 1:

* Mute while fast forward (similar to rewind).
* Disable all speech controls until there is actually text in the text area (i.e. until there is actually something to overlay/speak).
* Hide "Cancel Speech" button till festival actually starts speaking. 
* Think of a better way to communicate the Overlay button (e.g. make it clear that Overlay will commit the overlay to the video).
* Top menu is slightly obtrusive?? I mean I don't think it is but okay whatevs nasser
* Saving system is not entirely intuitive, needs to be changed

## END FEEDBACK ##

The video player must be able to:

* play video
* add an mp3 to the video
* add a synthetic speech overlay to the video

Must have the following controls:

*GENERAL:*
* open a file
* close a file

*VIDEO:*

* fast forward
* rewind
* play/pause
* stop

*AUDIO:* 

* volume up
* volume down
* mute

EXTRA FEATURES:

* .srt file generation to support subtitles
* translate subtitles using Google Translate API
* choose the voice for speech synthesization
* take screenshots
* have a graphical interface for adding in subtitles e.g. on the scrollbar, such that we can choose the subtitles, edit the subtitles, or delete the subtitles at that point
NOTE: possibly can use a sidebar instead
* Shia Laboeuf-ifier
* Registration page linking to paypal
* Pop-up ads because we're dicks
* Brightness controls and other video filters
* Playlist-type features (e.g. the ability to make a playlist)
* Full screen support
* Airhorn button

TOOLS WE CAN USE:

* build tools
	Gradle is pretty easy to use, but if Gradle isn't present on the UG4
	computers, we can use ANT instead.
* JavaFX
	Hopefully we can get it working on UG4 and to link up with vlcj
* Git + Bitbucket
	THIS IS NEEDED FOR THE ASSIGNMENT ANYWAY
* SceneBuilder
	if needed

ADDITIONAL NOTES:

* we can create an mp3 from festival using
	text2wave | lame -out.mp3

GUI DESIGN:

* We will be using .css to add skins to our gui. This enables us to possibly add interchangable skins.

* Our Buttons will have 3 states. On, Off and Hovered. On will show the user they have clicked a togglable button (such as pause). Hover will show when the user has there mouse above the button. Off will show when the button is toggled off (default).

* Perhaps a dark grey or near black background would fit well with our design. With glowing buttons.

* Our video timeline scroll bar should be hoverable aswell. Much like the Youtube scroll bar.

* We will need open video and save video buttons.

* Link the resizing of the window to the resizing of the mediaviews fitview



Screen Diagram:

![aww-board.png](https://bitbucket.org/repo/jBjpGX/images/3613898520-aww-board.png)

UML (Currently Working towards this on Refactor Branch):

![VidivoxUML.PNG](https://bitbucket.org/repo/jBjpGX/images/3845927551-VidivoxUML.PNG)