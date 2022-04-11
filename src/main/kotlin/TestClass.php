<?php

class TestClass
{

    public const T = "T";

    /**
     * @var string $str
     */
    private $str = "Teststring";

    /**
     * @var string $stri
     */
    private $stri = "Teststring";

    /**
     * this is just a comment
     */
    private $i = 1;


    function s()
    {
        echo $this->str;
        /** @var string $test */
        $test = "";
        TestClass::fromRecordData()
    }
}