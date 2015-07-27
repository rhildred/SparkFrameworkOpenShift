<?php 

$bInLayout = false;

function layout($sIn){
    global $bInLayout;
    global $attributes;
    if(!$bInLayout){
        $bInLayout = true;
        $sFile = $attributes->get("basedir") . $sIn; 
        //echo "layout" . $sFile;
        include $sFile;
        throw new Exception("don't print twice");
        
    }
}

function renderBody(){
    global $attributes;
    $sFile = $attributes->get("basedir") . $attributes->get("filename");
    //echo "render" . $sFile;
    include $sFile;
}

try{
    $test = $attributes->get("basedir") . $attributes->get("filename");
    include($test);    
}catch(Exception $e){
    //we want to throw out of here
}

?>