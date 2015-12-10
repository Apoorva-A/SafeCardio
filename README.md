# SafeCardio
SafeCardio Android Application, available on Google Play

SafeCardio helps those who exercise by both checking to see if they have fallen while running and also checking to 
see if they've been gone longer than they should've been.

This application, written using Java and Android Studio, accomplishes this by implementing a timer that resets as a
phone shakes, simulating running. The sensitive nature of the shake condition ensures that the only way the timer
doesn't reset is if you have fallen (presumably knocked unconcious or unable to move). If the timer reaches 0, it
sends a text message alert to a contact. Similarly, if you fail to return home in the pre-determined set of time
you entered, an alert is sent to an emergency contact.


