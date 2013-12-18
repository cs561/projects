//arel.Debug.activate();

var methodExists = function (object, method) {
    return typeof object !== 'undefined' && typeof method === 'function';
};

arel.sceneReady(function() {

    var scenario = {};
    scenario.objectName = "scenario";
    scenario.contents = []; // Array of all contents in this AR scenario
    scenario.trackables = []; // Array of all trackables in this AR scenario
    scenario.googleAnalytics = null;
    scenario.currentSceneID = "1";
    scenario.currentTrackingConfigPathOrIdentifier = "html/resources/TrackingData.zip";

    scenario.addObject = function (object) {
        arel.Debug.log("scenario.addObject(" + object.objectName + ")");
        this.registerObject(object);
        arel.Scene.addObject(object);
    };

    scenario.registerObject = function (object) {
        arel.Debug.log("scenario.registerObject(" + object.objectName + ")");
        arel.Events.setListener(object, this.objectEventsCallback, scenario);
    };

    scenario.groupID = 0;
    scenario.getNewGroupID = function () {
        this.groupID++;
        return this.groupID;
    };

    scenario.getTrackable = function (identifier) {
        arel.Debug.log("scenario.getTrackable(" + identifier + ")");
        var i;
        var trackable = null;
        if (!identifier || identifier === "") {
            arel.Debug.log("scenario.getTrackable(): Warning - identifier is empty, returning null");
            return trackable;
        }
        var allTrackables = this.trackables;
        allTrackables.push("instantTracker");
        for (i = 0; i < allTrackables.length; ++i) {
            trackable = allTrackables[i];
            if (trackable.objectName == identifier) {
                return trackable;
            }
            if (trackable.cosName == identifier) {
                return trackable;
            }
            if (trackable.cosID == identifier) {
                return trackable;
            }
        }
        arel.Debug.log("scenario.getTrackable(" + identifier + "): Error - could not correlate the given identifier to any known trackable.");
        return null;
    };

    scenario.sceneCallback = function (type, result) {
        if (!type) {
            return;
        }
        switch (type) {
        case arel.Events.Scene.ONTRACKING:
            this.onTrackingChanged(result);
            break;
        case arel.Events.Scene.ONVISUALSEARCHRESULT:
            break;
        case arel.Events.Scene.ONLOAD:
        case arel.Events.Scene.ONLOCATIONUPDATE:
        case arel.Events.Scene.ONREADY:
        default:
            break;
        }
    };

    scenario.objectEventsCallback = function (object, type, params) {
        switch (type) {
        case arel.Events.Object.ONREADY:
            if (methodExists(object, object.onLoaded)) {
                object.onLoaded();
            }
            // As a new object has just been loaded we need to re-trigger switchScene() to make sure only
            // content of the current scene is displayed.
            this.switchScene(this.currentSceneID);
            break;
        case arel.Events.Object.ONTOUCHSTARTED:
            if (this.googleAnalytics) {
                this.googleAnalytics.logUIInteraction(arel.Plugin.Analytics.Action.TOUCHSTARTED, object.getID());
            }
            break;
        case arel.Events.Object.ONTOUCHENDED:
            if (this.googleAnalytics) {
                this.googleAnalytics.logUIInteraction(arel.Plugin.Analytics.Action.TOUCHENDED, object.getID());
            }
            break;
        case arel.Events.Object.ONINVISIBLE:
        case arel.Events.Object.ONVISIBLE:
        case arel.Events.Object.ONANIMATIONENDED:
        case arel.Events.Object.ONMOVIEENDED:
        case arel.Events.Object.ONLOAD:
        case arel.Events.Object.ONROTATED:
        case arel.Events.Object.ONSCALED:
        case arel.Events.Object.ONTRANSLATED:
        default:
            break;
        }
    };

    scenario.onTrackingChanged = function (trackingValuesList) {
        if (trackingValuesList.length === 0) {
            arel.Debug.log("scenario.onTrackingChanged: Error - list of tracking values is empty, this should be impossible.");
            return;
        }
        var i, trackingValues, cosName, cosID, trackable, trackingMethod, gaTrackingMethod;
        for (i = 0; i < trackingValuesList.length; i++) {
            trackingValues = trackingValuesList[i];
            trackable = null;
            cosName = trackingValues.getCoordinateSystemName();
            cosID = trackingValues.getCoordinateSystemID();
            // Try to find the trackable by its COS name first. If that fails, try the COS ID.
            if (cosName && cosName !== "") {
                trackable = this.getTrackable(cosName);
            }
            if (trackable === null && cosID) {
                trackable = this.getTrackable(cosID);
            }
            if (trackable === null) {
                arel.Debug.log("onTrackingChanged: Error - Can't find a trackable matching COS name '" + cosName + "' or COS ID '" + cosID + "'");
                return;
            }

            switch (trackingValues.getState()) {
            case arel.Tracking.STATE_NOTTRACKING:
                arel.Debug.log("onTrackingChanged: " + trackable.objectName + " is not tracking");
                if (methodExists(trackable, trackable.onTrackingLost)) {
                    trackable.onTrackingLost(trackingValues);
                }
                break;
            case arel.Tracking.STATE_TRACKING:
                arel.Debug.log("onTrackingChanged: " + trackable.objectName + " is tracking");
                if (methodExists(trackable, trackable.onDetected)) {
                    trackable.onDetected();
                }
                if (methodExists(trackable, trackable.onTracked)) {
                    trackable.onTracked(trackingValues);
                }
                if (this.googleAnalytics) {
                    trackingMethod  = trackingValues.getType();
                    gaTrackingMethod = this.googleAnalytics.trackingTypeToAnalyticsType(trackingMethod);
                    this.googleAnalytics.logTrackingEvent(gaTrackingMethod, arel.Plugin.Analytics.Action.STATE_TRACKING, cosID, cosName);
                }
                break;
            case arel.Tracking.STATE_EXTRAPOLATED:
            case arel.Tracking.STATE_INITIALIZED:
            case arel.Tracking.STATE_REGISTERED:
            default:
                break;
            }
        }
    };

    scenario.showInstantTrackingContents = function () {
        arel.Debug.log("scenario.showInstantTrackingContents()");
        var i, content;
        for (i = 0; i < this.contents.length; ++i) {
            content = this.contents[i];
            if (!content.isModel3D || content.isShownOnTheUserDevice) {
                continue;
            }
            if (content.isShownOnTheInstantTracker) {
                if (methodExists(content, content.display)) {
                    content.display();
                }
            } else {
                if (methodExists(content, content.hide)) {
                    content.hide();
                }
            }
        }
    };

    scenario.hideInstantTrackingContents = function (displayNonInstantTrackingContents) {
        arel.Debug.log("scenario.hideInstantTrackingContents()");
        var i, content;
        for (i = 0; i < this.contents.length; ++i) {
            content = this.contents[i];
            if (!content.isModel3D || content.isShownOnTheUserDevice) {
                continue;
            }
            if (content.isShownOnTheInstantTracker) {
                if (methodExists(content, content.hide)) {
                    content.hide();
                }
            } else if (displayNonInstantTrackingContents) {
                if (methodExists(content, content.display)) {
                    content.display();
                }
            }
        }
    };

    scenario.startInstantTracking = function () {
        arel.Debug.log("scenario.startInstantTracking()");
        this.showInstantTrackingContents();
        arel.Scene.startInstantTracking(arel.Tracking.INSTANT2D);
        if (methodExists(this, this.onStartInstantTracking)) {
            this.onStartInstantTracking();
        }
    };

    scenario.stopInstantTracking = function () {
        arel.Debug.log("scenario.stopInstantTracking()");
        this.hideInstantTrackingContents(true);
        arel.Scene.setTrackingConfiguration(this.currentTrackingConfigPathOrIdentifier);
        if (methodExists(this, this.onStopInstantTracking)) {
            this.onStopInstantTracking();
        }
    };

    scenario.switchScene = function (newSceneID) {
        arel.Debug.log("scenario.switchScene(" + newSceneID + ")");
        if (newSceneID) {
            var sceneExists = false;
            var i, content;
            for (i = 0; i < this.contents.length; ++i) {
                content = this.contents[i];
                if (content.sceneID == newSceneID) {
                    sceneExists = true;
                    break;
                }
            }
            if (!sceneExists) {
                arel.Debug.log("scenario.switchScene(): Error - No scene with ID " + newSceneID + " exists.");
                return;
            }
        }

        this.currentSceneID = newSceneID;

        for (i = 0; i < this.contents.length; ++i) {
            content = this.contents[i];
            // Skip contents which aren't 3D models or which have been explicitly set invisible.
            if (!content.isModel3D || content.isExplicitlyInvisible) {
                continue;
            }
            // Display this content if it is part of the current scene, if no current scene is set at all or the content has no scene.
            if (content.sceneID == this.currentSceneID || !this.currentSceneID || !content.sceneID) {
                if (methodExists(content, content.display)) {
                    content.display(true);
                }
            } else {
                if (methodExists(content, content.hide)) {
                    content.hide(true);
                }
            }
        }

        // Iterate over all trackables, simulate an onDetected() and onTracked() event for all those which are currently tracking.
        var trackable;
        for (i = 0; i < this.trackables.length; ++i) {
            trackable = this.trackables[i];
            if (trackable.isCurrentlyTracking) {
                if (methodExists(trackable, trackable.onDetected)) {
                    trackable.onDetected();
                }
                if (methodExists(trackable, trackable.onTracked)) {
                    trackable.onTracked(trackable.currentTrackingValues);
                }
            }
        }

        if (methodExists(this, this.onSwitchScene)) {
            this.onSwitchScene(this.currentSceneID);
        }
    };

    scenario.skipTrackingInitialization = function () {
        arel.Debug.log("scenario.skipTrackingInitialization()");
        arel.Scene.sensorCommand("initialize", "", function(a) {});
        if (methodExists(this, this.onSkipTrackingInitialization)) {
            this.onSkipTrackingInitialization();
        }
    };

    scenario.reloadTrackingConfiguration = function () {
        arel.Debug.log("scenario.reloadTrackingConfiguration()");
        arel.Scene.setTrackingConfiguration(this.currentTrackingConfigPathOrIdentifier);
        if (methodExists(this, this.onReloadTrackingConfiguration)) {
            this.onReloadTrackingConfiguration();
        }
    };

    scenario.onStartup = function () {
        arel.Debug.log("Welcome to the 'New project' Augmented Reality experience.");

        arel.Events.setListener(arel.Scene, scenario.sceneCallback, scenario);

        if (google_analytics_id) {
            arel.Debug.log("Google Analytics is enabled. Your account ID is: " + google_analytics_id);
            arel.Debug.log("The event sampling rate is: arel.Plugin.Analytics.EventSampling.ONCE");
            scenario.googleAnalytics = new arel.Plugin.Analytics(google_analytics_id, arel.Plugin.Analytics.EventSampling.ONCE, "");
        } else {
            arel.Debug.log("Note: No Google Analytics ID is set - Google Analytics will be disabled.");
        }

        if (methodExists(scenario, scenario.onLoaded)) {
            scenario.onLoaded();
        }

        // The following contents have been defined in the index.xml file, therefore we need to register them
        // and call their onLoaded() event manually.
        scenario.registerObject(video2);
        if (methodExists(video2, video2.onLoaded)) {
            video2.onLoaded();
        }
        scenario.registerObject(image2);
        if (methodExists(image2, image2.onLoaded)) {
            image2.onLoaded();
        }
        scenario.registerObject(image3);
        if (methodExists(image3, image3.onLoaded)) {
            image3.onLoaded();
        }
        scenario.registerObject(image4);
        if (methodExists(image4, image4.onLoaded)) {
            image4.onLoaded();
        }
        scenario.registerObject(image5);
        if (methodExists(image5, image5.onLoaded)) {
            image5.onLoaded();
        }

        scenario.hideInstantTrackingContents();

        if (methodExists(userDevice, userDevice.onLoaded)) {
            userDevice.onLoaded();
        }

        // All objects have been defined, so start the AR experience by calling each trackable's .onLoaded() method.
        var i, trackable;
        for (i = 0; i < scenario.trackables.length; ++i) {
            trackable = scenario.trackables[i];
            if (methodExists(trackable, trackable.onLoaded)) {
                trackable.onLoaded();
            }
        }
        // Call switchScene() once with the inital scene ID to make sure only content of that scene is visible.
        this.switchScene(this.currentSceneID);
    };


    // ▶ Max Herre - A-N-N-A - MTV Unplugged (Official Teaser) - YouTube
    var video2 = arel.Scene.getObject("video2");
    video2.objectName = "video2";
    video2.sceneID = "1";
    video2.isModel3D = true;
    video2.isExplicitlyInvisible = false;
    video2.isShownOnTheUserDevice = false;
    video2.isShownOnTheInstantTracker = false;
    scenario.contents.push(video2);

    video2.setSceneID = function (sceneID) {
        this.sceneID = sceneID;
        scenario.switchScene(scenario.currentSceneID);
    };

    video2.isLoaded = function () {
        return arel.Scene.objectExists("video2");
    };

    video2.bind = function (cosID) {
        arel.Debug.log(this.objectName + ".bind(" + cosID + ")");
        this.setCoordinateSystemID(cosID);
    };

    video2.load = function () {
        arel.Debug.log(this.objectName + ".load()");
        if (!this.isLoaded()) {
            scenario.addObject(this);
        }
    };

    video2.unload = function () {
        arel.Debug.log(this.objectName + ".unload()");
        if (this.isLoaded()) {
            arel.Scene.removeObject(this);
            if (methodExists(this, this.onUnloaded)) {
                this.onUnloaded();
            }
        }
    };

    video2.display = function (internalCall) {
        arel.Debug.log(this.objectName + ".display()");
        if (this.sceneID != scenario.currentSceneID) {
            return;
        }
        this.setVisibility(true);
        if (!internalCall) {
            this.isExplicitlyInvisible = false;
        }
    };

    video2.hide = function (internalCall) {
        arel.Debug.log(this.objectName + ".hide()");
        this.setVisibility(false);
        if (!internalCall) {
            this.isExplicitlyInvisible = true;
        }
    };

    video2.attach = function (origin, offset) {
        arel.Debug.log(this.objectName + ".attach(" + origin.objectName + ")");
        this.setTranslation(arel.Vector3D.add(origin.getTranslation(), offset));
        if (origin.groupID) {
            if (this.groupID) {
                arel.GestureHandler.removeObject(video2);
            }
            arel.GestureHandler.addObject("video2", origin.groupID);
            this.setPickingEnabled(false);
        }
    };

    video2.play = function () {
        if (!this.isLoaded()) {
            arel.Debug.log("Error: " + this.objectName + ".play() called, but " + this.objectName + " is not loaded yet.");
            return;
        }
        arel.Debug.log(this.objectName + ".play()");
        this.startMovieTexture(true);
        if (scenario.googleAnalytics) {
            scenario.googleAnalytics.logVideo("▶ Max Herre - A-N-N-A - MTV Unplugged (Official Teaser) - YouTube");
        }
        if (methodExists(this, this.onPlayed)) {
            this.onPlayed();
        }
    };

    video2.pause = function () {
        arel.Debug.log(this.objectName + ".pause()");
        this.pauseMovieTexture();
        if (methodExists(this, this.onPaused)) {
            this.onPaused();
        }
    };

    video2.stop = function () {
        arel.Debug.log(this.objectName + ".stop()");
        this.stopMovieTexture();
    };


    // 1
    var image2 = arel.Scene.getObject("image2");
    image2.objectName = "image2";
    image2.sceneID = "1";
    image2.isModel3D = true;
    image2.isExplicitlyInvisible = false;
    image2.isShownOnTheUserDevice = false;
    image2.isShownOnTheInstantTracker = false;
    scenario.contents.push(image2);

    image2.setSceneID = function (sceneID) {
        this.sceneID = sceneID;
        scenario.switchScene(scenario.currentSceneID);
    };

    image2.isLoaded = function () {
        return arel.Scene.objectExists("image2");
    };

    image2.bind = function (cosID) {
        arel.Debug.log(this.objectName + ".bind(" + cosID + ")");
        this.setCoordinateSystemID(cosID);
    };

    image2.load = function () {
        arel.Debug.log(this.objectName + ".load()");
        if (!this.isLoaded()) {
            scenario.addObject(this);
        }
    };

    image2.unload = function () {
        arel.Debug.log(this.objectName + ".unload()");
        if (this.isLoaded()) {
            arel.Scene.removeObject(this);
            if (methodExists(this, this.onUnloaded)) {
                this.onUnloaded();
            }
        }
    };

    image2.display = function (internalCall) {
        arel.Debug.log(this.objectName + ".display()");
        if (this.sceneID != scenario.currentSceneID) {
            return;
        }
        this.setVisibility(true);
        if (!internalCall) {
            this.isExplicitlyInvisible = false;
        }
    };

    image2.hide = function (internalCall) {
        arel.Debug.log(this.objectName + ".hide()");
        this.setVisibility(false);
        if (!internalCall) {
            this.isExplicitlyInvisible = true;
        }
    };

    image2.attach = function (origin, offset) {
        arel.Debug.log(this.objectName + ".attach(" + origin.objectName + ")");
        this.setTranslation(arel.Vector3D.add(origin.getTranslation(), offset));
        if (origin.groupID) {
            if (this.groupID) {
                arel.GestureHandler.removeObject(image2);
            }
            arel.GestureHandler.addObject("image2", origin.groupID);
            this.setPickingEnabled(false);
        }
    };


    // 2
    var image3 = arel.Scene.getObject("image3");
    image3.objectName = "image3";
    image3.sceneID = "1";
    image3.isModel3D = true;
    image3.isExplicitlyInvisible = false;
    image3.isShownOnTheUserDevice = false;
    image3.isShownOnTheInstantTracker = false;
    scenario.contents.push(image3);

    image3.setSceneID = function (sceneID) {
        this.sceneID = sceneID;
        scenario.switchScene(scenario.currentSceneID);
    };

    image3.isLoaded = function () {
        return arel.Scene.objectExists("image3");
    };

    image3.bind = function (cosID) {
        arel.Debug.log(this.objectName + ".bind(" + cosID + ")");
        this.setCoordinateSystemID(cosID);
    };

    image3.load = function () {
        arel.Debug.log(this.objectName + ".load()");
        if (!this.isLoaded()) {
            scenario.addObject(this);
        }
    };

    image3.unload = function () {
        arel.Debug.log(this.objectName + ".unload()");
        if (this.isLoaded()) {
            arel.Scene.removeObject(this);
            if (methodExists(this, this.onUnloaded)) {
                this.onUnloaded();
            }
        }
    };

    image3.display = function (internalCall) {
        arel.Debug.log(this.objectName + ".display()");
        if (this.sceneID != scenario.currentSceneID) {
            return;
        }
        this.setVisibility(true);
        if (!internalCall) {
            this.isExplicitlyInvisible = false;
        }
    };

    image3.hide = function (internalCall) {
        arel.Debug.log(this.objectName + ".hide()");
        this.setVisibility(false);
        if (!internalCall) {
            this.isExplicitlyInvisible = true;
        }
    };

    image3.attach = function (origin, offset) {
        arel.Debug.log(this.objectName + ".attach(" + origin.objectName + ")");
        this.setTranslation(arel.Vector3D.add(origin.getTranslation(), offset));
        if (origin.groupID) {
            if (this.groupID) {
                arel.GestureHandler.removeObject(image3);
            }
            arel.GestureHandler.addObject("image3", origin.groupID);
            this.setPickingEnabled(false);
        }
    };


    // 3
    var image4 = arel.Scene.getObject("image4");
    image4.objectName = "image4";
    image4.sceneID = "1";
    image4.isModel3D = true;
    image4.isExplicitlyInvisible = false;
    image4.isShownOnTheUserDevice = false;
    image4.isShownOnTheInstantTracker = false;
    scenario.contents.push(image4);

    image4.setSceneID = function (sceneID) {
        this.sceneID = sceneID;
        scenario.switchScene(scenario.currentSceneID);
    };

    image4.isLoaded = function () {
        return arel.Scene.objectExists("image4");
    };

    image4.bind = function (cosID) {
        arel.Debug.log(this.objectName + ".bind(" + cosID + ")");
        this.setCoordinateSystemID(cosID);
    };

    image4.load = function () {
        arel.Debug.log(this.objectName + ".load()");
        if (!this.isLoaded()) {
            scenario.addObject(this);
        }
    };

    image4.unload = function () {
        arel.Debug.log(this.objectName + ".unload()");
        if (this.isLoaded()) {
            arel.Scene.removeObject(this);
            if (methodExists(this, this.onUnloaded)) {
                this.onUnloaded();
            }
        }
    };

    image4.display = function (internalCall) {
        arel.Debug.log(this.objectName + ".display()");
        if (this.sceneID != scenario.currentSceneID) {
            return;
        }
        this.setVisibility(true);
        if (!internalCall) {
            this.isExplicitlyInvisible = false;
        }
    };

    image4.hide = function (internalCall) {
        arel.Debug.log(this.objectName + ".hide()");
        this.setVisibility(false);
        if (!internalCall) {
            this.isExplicitlyInvisible = true;
        }
    };

    image4.attach = function (origin, offset) {
        arel.Debug.log(this.objectName + ".attach(" + origin.objectName + ")");
        this.setTranslation(arel.Vector3D.add(origin.getTranslation(), offset));
        if (origin.groupID) {
            if (this.groupID) {
                arel.GestureHandler.removeObject(image4);
            }
            arel.GestureHandler.addObject("image4", origin.groupID);
            this.setPickingEnabled(false);
        }
    };


    // 4
    var image5 = arel.Scene.getObject("image5");
    image5.objectName = "image5";
    image5.sceneID = "1";
    image5.isModel3D = true;
    image5.isExplicitlyInvisible = false;
    image5.isShownOnTheUserDevice = false;
    image5.isShownOnTheInstantTracker = false;
    scenario.contents.push(image5);

    image5.setSceneID = function (sceneID) {
        this.sceneID = sceneID;
        scenario.switchScene(scenario.currentSceneID);
    };

    image5.isLoaded = function () {
        return arel.Scene.objectExists("image5");
    };

    image5.bind = function (cosID) {
        arel.Debug.log(this.objectName + ".bind(" + cosID + ")");
        this.setCoordinateSystemID(cosID);
    };

    image5.load = function () {
        arel.Debug.log(this.objectName + ".load()");
        if (!this.isLoaded()) {
            scenario.addObject(this);
        }
    };

    image5.unload = function () {
        arel.Debug.log(this.objectName + ".unload()");
        if (this.isLoaded()) {
            arel.Scene.removeObject(this);
            if (methodExists(this, this.onUnloaded)) {
                this.onUnloaded();
            }
        }
    };

    image5.display = function (internalCall) {
        arel.Debug.log(this.objectName + ".display()");
        if (this.sceneID != scenario.currentSceneID) {
            return;
        }
        this.setVisibility(true);
        if (!internalCall) {
            this.isExplicitlyInvisible = false;
        }
    };

    image5.hide = function (internalCall) {
        arel.Debug.log(this.objectName + ".hide()");
        this.setVisibility(false);
        if (!internalCall) {
            this.isExplicitlyInvisible = true;
        }
    };

    image5.attach = function (origin, offset) {
        arel.Debug.log(this.objectName + ".attach(" + origin.objectName + ")");
        this.setTranslation(arel.Vector3D.add(origin.getTranslation(), offset));
        if (origin.groupID) {
            if (this.groupID) {
                arel.GestureHandler.removeObject(image5);
            }
            arel.GestureHandler.addObject("image5", origin.groupID);
            this.setPickingEnabled(false);
        }
    };
	
	 // Mammut
    var model7 = arel.Scene.getObject("model7");
    model7.objectName = "model7";
    model7.sceneID = "1";
    model7.isModel3D = true;
    model7.isExplicitlyInvisible = false;
    model7.isShownOnTheUserDevice = false;
    model7.isShownOnTheInstantTracker = false;
    scenario.contents.push(model7);

    model7.setSceneID = function (sceneID) {
        this.sceneID = sceneID;
        scenario.switchScene(scenario.currentSceneID);
    };

    model7.isLoaded = function () {
        return arel.Scene.objectExists("model7");
    };

    model7.bind = function (cosID) {
        arel.Debug.log(this.objectName + ".bind(" + cosID + ")");
        this.setCoordinateSystemID(cosID);
    };

    model7.load = function () {
        arel.Debug.log(this.objectName + ".load()");
        if (!this.isLoaded()) {
            scenario.addObject(this);
        }
    };

    model7.unload = function () {
        arel.Debug.log(this.objectName + ".unload()");
        if (this.isLoaded()) {
            arel.Scene.removeObject(this);
            if (methodExists(this, this.onUnloaded)) {
                this.onUnloaded();
            }
        }
    };

    model7.display = function (internalCall) {
        arel.Debug.log(this.objectName + ".display()");
        if (this.sceneID != scenario.currentSceneID) {
            return;
        }
        this.setVisibility(true);
        if (!internalCall) {
            this.isExplicitlyInvisible = false;
        }
    };

    model7.hide = function (internalCall) {
        arel.Debug.log(this.objectName + ".hide()");
        this.setVisibility(false);
        if (!internalCall) {
            this.isExplicitlyInvisible = true;
        }
    };

    model7.attach = function (origin, offset) {
        arel.Debug.log(this.objectName + ".attach(" + origin.objectName + ")");
        this.setTranslation(arel.Vector3D.add(origin.getTranslation(), offset));
        if (origin.groupID) {
            if (this.groupID) {
                arel.GestureHandler.removeObject(model7);
            }
            arel.GestureHandler.addObject("model7", origin.groupID);
            this.setPickingEnabled(false);
        }
    };

    model7.play = function (animationName, animationLooped) {
        arel.Debug.log(this.objectName + ".play(" + animationName + ", " + animationLooped + ")");
        this.startAnimation(animationName, animationLooped);
        if (methodExists(this, this.onPlayed)) {
            this.onPlayed(animationName);
        }
    };


    var userDevice = {};
    userDevice.objectName = "userDevice";
    userDevice.cosName = "Device";
    userDevice.cosID = "-1";

    var instantTracker = {};
    scenario.trackables.push(instantTracker);
    instantTracker.objectName = "instantTracker";
    instantTracker.cosName = "InstantTracker";
    instantTracker.cosID = "1";
    instantTracker.isCurrentlyTracking = false;
    instantTracker.currentTrackingValues = null;

    var pattern5 = {};
    scenario.trackables.push(pattern5);
    pattern5.objectName = "pattern5";
    pattern5.cosName = "Video_1";
    pattern5.cosID = "1";
    pattern5.isCurrentlyTracking = false;
    pattern5.currentTrackingValues = null;
    pattern5.onTracked = function (trackingValues) {
        arel.Debug.log(this.objectName + ".onTracked()");
        this.isCurrentlyTracking = true;
        this.currentTrackingValues = trackingValues;
        video2.play();
    };

    pattern5.onTrackingLost = function (trackingValues) {
        arel.Debug.log(this.objectName + ".onTrackingLost()");
        this.isCurrentlyTracking = false;
        this.currentTrackingValues = null;
        video2.pause();
    };

    pattern5.onUnloaded = function () {
        arel.Debug.log(this.objectName + ".onUnloaded()");
        video2.unload();
    };


    var pattern7 = {};
    scenario.trackables.push(pattern7);
    pattern7.objectName = "pattern7";
    pattern7.cosName = "Target_2";
    pattern7.cosID = "2";
    pattern7.isCurrentlyTracking = false;
    pattern7.currentTrackingValues = null;
    pattern7.onTrackingLost = function (trackingValues) {
        arel.Debug.log(this.objectName + ".onTrackingLost()");
        this.isCurrentlyTracking = false;
        this.currentTrackingValues = null;
        /**** Begin of custom script ****/
        
        	if(image2.getVisibility().liveview){
                image2.hide();
                image3.setVisibility(true,true,true);
            }else if(image3.getVisibility().liveview){
                image3.hide();
                image4.setVisibility(true,true,true);
            }else if(image4.getVisibility().liveview){
        		image4.hide();
        		image5.setVisibility(true,true,true);
        	}else if (image5.getVisibility().liveview){
        		image5.hide();
        		image2.setVisibility(true,true,true);
        	}
        
        /***** End of custom script *****/
    };

    pattern7.onLoaded = function () {
        arel.Debug.log(this.objectName + ".onLoaded()");
        /**** Begin of custom script ****/
        
        	image3.hide();
        	image4.hide();
        	image5.hide();
        
        /***** End of custom script *****/
    };

    pattern7.onUnloaded = function () {
        arel.Debug.log(this.objectName + ".onUnloaded()");
        image3.unload();
        image4.unload();
        image5.unload();
        image2.unload();
    };
	
	var pattern8 = {};
    scenario.trackables.push(pattern8);
    pattern8.objectName = "pattern8";
    pattern8.cosName = "Werbung_1";
    pattern8.cosID = "3";
    pattern8.isCurrentlyTracking = false;
    pattern8.currentTrackingValues = null;
    pattern8.onUnloaded = function () {
        arel.Debug.log(this.objectName + ".onUnloaded()");
        model7.unload();
    };


    // Kick-off the AR experience by calling the scenario's onStartup() method as soon as AREL is ready
    scenario.onStartup();
});