<?php

class T
{

    /**
     * @var string $str
     */
    private $str = "Teststring";

    /**
     * this is just a comment
     */
    private $i = 1;

    function s()
    {
        echo $this->str;
        /** @var string $test */
        $test = "";
    }
}