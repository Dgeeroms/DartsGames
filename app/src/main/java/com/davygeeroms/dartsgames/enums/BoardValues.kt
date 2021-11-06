package com.davygeeroms.dartsgames.enums

enum class BoardValues(val valuesStr: String, val modifier: Int, val id: Int, val valueNumber : Int) {

    //ID:
    //MISS: 0
    //SINGLES: 1 -> 21 (incl bull = 21)
    //DOUBLES: 22 -> 42 (incl bullseye = 42)
    //TRIPLES: 43 -> 62

    S20("Twenty"            , 1,20,20),
    D20("Double Twenty"     , 2,41,20),
    T20("Triple Twenty"     , 3,62,20),
    S19("Nineteen"          , 1,19,19),
    D19("Double Nineteen"   , 2,40,19),
    T19("Triple Nineteen"   , 3,61,19),
    S18("Eighteen"          , 1,18,18),
    D18("Double Eighteen"   , 2,39,18),
    T18("Triple Eighteen"   , 3,60,18),
    S17("Seventeen"         , 1,17,17),
    D17("Double Seventeen"  , 2,38,17),
    T17("Triple Seventeen"  , 3,59,17),
    S16("Sixteen"           , 1,16,16),
    D16("Double Sixteen"    , 2,37,16),
    T16("Triple Sixteen"    , 3,58,16),
    S15("Fifteen"           , 1,15,15),
    D15("Double Fifteen"    , 2,36,15),
    T15("Triple Fifteen"    , 3,57,15),
    S14("Fourteen"          , 1,14,14),
    D14("Double Fourteen"   , 2,35,14),
    T14("Triple Fourteen"   , 3,56,14),
    S13("Thirteen"          , 1,13,13),
    D13("Double Thirteen"   , 2,34,13),
    T13("Triple Thirteen"   , 3,55,13),
    S12("Twelve"            , 1,12,12),
    D12("Double Twelve"     , 2,33,12),
    T12("Triple Twelve"     , 3,54,12),
    S11("Eleven"            , 1,11,11),
    D11("Double Eleven"     , 2,32,11),
    T11("Triple Eleven"     , 3,53,11),
    S10("Ten"               , 1,10,10),
    D10("Double Ten"        , 2,31,10),
    T10("Triple Ten"        , 3,52,10),
    S09("Nine"              , 1,9,9),
    D09("Double Nine"       , 2,30,9),
    T09("Triple Nine"       , 3,51,9),
    S08("Eight"             , 1,8,8),
    D08("Double Eight"      , 2,29,8),
    T08("Triple Eight"      , 3,50,8),
    S07("Seven"             , 1,7,7),
    D07("Double Seven"      , 2,28,7),
    T07("Triple Seven"      , 3,49,7),
    S06("Six"               , 1,6,6),
    D06("Double Six"        , 2,27,6),
    T06("Triple Six"        , 3,48,6),
    S05("Five"              , 1,5,5),
    D05("Double Five"       , 2,26,5),
    T05("Triple Five"       , 3,47,5),
    S04("Four"              , 1,4,4),
    D04("Double Four"       , 2,25,4),
    T04("Triple Four"       , 3,46,4),
    S03("Three"             , 1,3,3),
    D03("Double Three"      , 2,24,3),
    T03("Triple Three"      , 3,45,3),
    S02("Two"               , 1,2,2),
    D02("Double Two"        , 2,23,2),
    T02("Triple Two"        , 3,44,2),
    S01("One"               , 1,1,1),
    D01("Double One"        , 2,22,1),
    T01("Triple One"        , 3,43,1),
    S25("Bull"              , 1,21,25),
    D25("Bullseye"          , 2,42,25),
    S00("MISS"              , 1,0,0);

    companion object {
        fun valueOf(value: Int?): BoardValues? = BoardValues.values().find { it.id == value }
    }

}