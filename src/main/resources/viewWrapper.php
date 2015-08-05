<?php

$bInLayout = false;

function layout($sIn){
    global $bInLayout;
    global $basedir;
    global $model;
    if(!$bInLayout){
        $bInLayout = true;
        $sFile = $basedir . $sIn;
        //echo "layout" . $sFile;
        include $sFile;
        throw new Exception("don't print twice");

    }
}

function renderBody(){
    global $basedir;
    global $filename;
    global $model;
    $sFile = $basedir . $filename;
    //echo "render" . $sFile;
    include $sFile;
}

try{
    $sFile = $basedir . $filename;
    include($sFile);
}catch(Exception $e){
    //we want to throw out of here
}

?>
