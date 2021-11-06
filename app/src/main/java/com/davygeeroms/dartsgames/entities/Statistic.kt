package com.davygeeroms.dartsgames.entities

class Statistic(var totalScore: Int = 0,
                var totalThrows: Int = 0,
                var highestThrown: Int = 0,
                var highestCheckOut: Int = 0,
                var misses: Int = 0,
                var hits: Int = 0) {

    fun updateStats(bvs: List<BoardValue>){

        var turnScore = 0

        for(bv in bvs){
            totalThrows++
            turnScore += bv.value * bv.modifier
        }

        if (highestThrown < turnScore){
            highestThrown = turnScore
        }
        totalScore += turnScore
    }

    fun getAvg(): Float{
        return totalScore.toFloat() / totalThrows
    }

    fun getHitPct(): Float{
        return (hits.toFloat() / totalThrows) * 100F
    }

    fun getMissPct(): Float{
        return (misses.toFloat() / totalThrows) * 100F
    }

    fun updateCheckOut(bvs: List<BoardValue>){

        var turnScore = 0

        for(bv in bvs){
            totalThrows++
            turnScore += bv.value * bv.modifier
        }

        if (highestCheckOut < turnScore){
            highestCheckOut = turnScore
        }
        totalScore += turnScore
    }

    fun hit(){
        hits++
    }

    fun miss(){
        misses++
    }

    override fun equals(other: Any?): Boolean {

        return hashCode() == (other as Statistic).hashCode()
    }

    override fun hashCode(): Int {
        var result = totalScore.hashCode()
        result = 31 * result + totalThrows.hashCode()
        result = 31 * result + highestThrown.hashCode()
        result = 31 * result + highestCheckOut.hashCode()
        result = 31 * result + misses.hashCode()
        result = 31 * result + hits.hashCode()
        return result
    }

}