=============================
# VIDIVOX PLANNING - 01.02 #
=============================

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