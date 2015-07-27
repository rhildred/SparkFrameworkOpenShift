<?php

require_once(dirname(__FILE__) . '/vendor/autoload.php');

$app = new \Slim\Slim(array(
    'view' => new \PHPView\PHPView(),
    'templates.path' => __DIR__ . "/views/"));

return $app->render($filename, array(page=>$sPage));


