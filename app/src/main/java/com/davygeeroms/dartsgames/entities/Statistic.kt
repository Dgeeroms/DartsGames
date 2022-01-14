package com.davygeeroms.dartsgames.entities

/**
 * A statistic - collection of data related to score for this game
 * All stats default to 0
 * @param totalScore: Accumulation of all score thrown
 * @param totalThrows: How many times has the player thrown
 * @param highestThrown: Record of the highest score thrown
 * @param highestCheckOut: Support for multiple legs - highest leg-ending score is recorded
 * @param misses: How many times the target was missed. Applies better to game modes with dedicated targets
 * @param hits: How many times the target was hit. Applies better to game modes with dedicated targets
 */
class Statistic(var totalScore: Int = 0,
                var totalThrows: Int = 0,
                var highestThrown: Int = 0,
                var highestCheckOut: Int = 0,
                var misses: Int = 0,
                var hits: Int = 0) {
    /**
     * Update the statistics based on list of thrown darts
     * @param bvs: List of thrown darts
     */
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

    /**
     * Takes the total accumulated score and divides by total throws
     * @return average score per thrown dart during this game
     */
    fun getAvg(): Float{
        return totalScore.toFloat() / totalThrows
    }

    /**
     * Calculates the percentage of hits compared to total of throws
     * @return hit percentage
     */
    fun getHitPct(): Float{
        return (hits.toFloat() / totalThrows) * 100F
    }

    /**
     * Calculates the percentage of misses compared to total of throws
     * @return miss percentage
     */
    fun getMissPct(): Float{
        return (misses.toFloat() / totalThrows) * 100F
    }

    /**
     * If player throws leg-winning darts; calculate and update checkout value
     * @param bvs: List of thrown darts that ended the leg
     */
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

    /**
     * Increment hit counter
     */
    fun hit(){
        hits++
    }

    /**
     * Increment miss counter
     */
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