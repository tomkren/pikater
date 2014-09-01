/*
 * First of all, load video.js:
 */
 
/*
includeJS("docs/video.js", function()
{
    videojs.options.flash.swf = "docs/video-js.swf";
});
includeCSS("docs/video-js.css");
*/
includeJS("http://vjs.zencdn.net/4.7/video.js");
includeCSS("http://vjs.zencdn.net/4.7/video-js.css");

/*
 * Then we have to dynamically load <video> elements (HTML5) based on the given ID,
 * because markdown filters them out...
 */

// we do that by setting up an onload hook (markdown has obviously not been parsed and rendered yet...)
window.onload = function()
{
    /*
     * When the page is fully loaded:
     */
     
    // alert("Start!");
    var videoContainers = document.getElementsByClassName("embeddedVideo");
    if(videoContainers)
    {
        // alert("About to add!");
        for (var i = 0; i < videoContainers.length; i++)
        {
            var currentContainer = videoContainers[i];
            currentContainer.style.width = "640px";
            currentContainer.style.height = "264px";
            
            var childInfo = currentContainer.getElementsByTagName("span");
            currentContainer.appendChild(constructVideoElem(
                "video_nr_" + (i + 1),
                childInfo[0].textContent,
                childInfo[1].textContent
            ));
            
            // don't mistake this for an error... it works!
            currentContainer.removeChild(childInfo[0]);
            currentContainer.removeChild(childInfo[0]);
        }
        // alert("Added!");
    }
};

/********************************************************/
/* VIDEO.JS HELPER FUNCTIONS */

function constructVideoElem(currentID, videoType, videoURL)
{
    var srcElem = document.createElement("source");
    srcElem.setAttribute("src", videoURL);
    srcElem.setAttribute("type", videoType);
    
    var altElem = document.createElement("p");
    altElem.className = "vjs-no-js";
    altElem.innerHTML = "To view this video please enable JavaScript, and consider upgrading to a web browser that <a href=\"http://videojs.com/html5-video-support/\" target=\"_blank\">supports HTML5 video</a>";
    
    var videoElem = document.createElement("video");
    videoElem.id = currentID;
    videoElem.className = "video-js vjs-default-skin vjs-big-play-centered";
    videoElem.setAttributeNode(document.createAttribute("controls"));
    videoElem.setAttribute("data-setup", "{ \"width\": \"100%\", \"height\": \"100%\" }");
    videoElem.appendChild(srcElem);
    videoElem.appendChild(altElem);
    
    return videoElem;
}

/********************************************************/
/* DOM HELPER FUNCTIONS */

function includeJS(path, fOnLoad)
{
    var scriptElem = document.createElement('script');
    scriptElem.src = path;
    scriptElem.type = "text\/javascript";
    if(fOnLoad)
    {
        // depending on the browser, one of the following 2 will be called:
        scriptElem.onreadystatechange = function ()
        {
            if (this.readyState == 'complete')
            {
                fOnLoad();
            }
        }
        scriptElem.onload = fOnLoad;
   }
   includeElementIntoHead(scriptElem);
}

function includeCSS(path)
{
    var linkElem = document.createElement( "link" );
    linkElem.rel = "stylesheet";
    linkElem.type = "text\/css";
    linkElem.href = path;
    includeElementIntoHead(linkElem);
}

function includeElementIntoHead(elem)
{
    document.head.appendChild(elem);
}