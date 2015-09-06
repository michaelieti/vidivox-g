=====================
# VIDIVOX PLANNING - 01 #
=====================

The video player must be able to:

* play video
* add an mp3 to the video
* add a synthetic speech overlay to the video

Must have the following controls:

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

* text generation for subtitles
* hack into Google API and get auto-translated text (or make your own language)
* choose the voice for speech synthesization
* take screenshots
* have a graphical interface for adding in subtitles e.g. on the scrollbar, such that we can choose the subtitles, edit the subtitles, or delete the subtitles at that point
* Shia Laboeuf-ifier
* Registration page linking to paypal
* Pop-up ads because we're dicks

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