package amazonQuestionFromLC;

import java.util.Deque;
import java.util.LinkedList;

public class TrappingRainWater {
	/*
	 * Solution 1: Find the leftMax and rightMax of each bar and the water each bar 
	 * 		can store is min(leftMax, rightMax) - current_bar. Add all water in each
	 * 		bar together and then we can get the result.
	 * T: O(n^2) 
	 * S: O(n)
	 */
	class Pair {
		int leftMax;
		int rightMax;
		int self;

		public Pair(int l, int r, int s) {
			leftMax = l;
			rightMax = r;
			self = s;
		}
	}

	public int trap(int[] height) {
		// corner case
		if (height == null || height.length == 0) {
			return 0;
		}
		int len = height.length;
		Pair[] pairs = new Pair[len];
		for (int i = 0; i < len; ++i) {
			int left = 0;
			int right = 0;
			for (int j = 0; j <= i; ++j) {
				// find leftMax
				if (height[j] > left) {
					left = height[j];
				}
			}
			for (int j = i; j < len; ++j) {
				// find rightMax
				if (height[j] > right) {
					right = height[j];
				}
			}
			pairs[i] = new Pair(left, right, height[i]);
		}
		int ans = 0;
		for (int i = 0; i < len; ++i) {
			ans += Math.min(pairs[i].leftMax, pairs[i].rightMax) - pairs[i].self;
		}
		return ans;
	}
	
	/*
	 * Algorithm 2: Dynamic programming
	 * 	Since the leftMax of ith bar is always max(leftMax(i-1), height(i)), we can use
	 * O(n) to find the leftMax and similarly rightMax for ith bar.
	 * 
	 * T: O(n)
	 * S: O(n)
	 * */
	public int trapII(int[] height) {
		// corner case
		if (height == null || height.length == 0) {
			return 0;
		}
		int len = height.length;
		int[] leftMax = new int[len];
		int[] rightMax = new int[len];
		leftMax[0] = height[0];
		for (int i = 1; i < len; ++i) {
			leftMax[i] = Math.max(leftMax[i - 1], height[i]);
		}
		rightMax[len - 1] = height[len - 1];
		for (int i = len - 2; i >= 0; --i) {
			rightMax[i] = Math.max(rightMax[i + 1], height[i]);
		}
		int ans = 0;
		for (int i = 0; i < len; ++i) {
			ans += Math.min(leftMax[i], rightMax[i]) - height[i];
		}
		return ans;
	}
	/*
	 * Data structure: stack
	 * Algorithm 3:  (one iteration)
	 *  we push indices of bars into the stack, if cur.height < stack.top, it means 
	 *  the left is always larger than current bar, so we can continue pushing.
	 *  else: we found rightMax,  we need to pop out top, and find bound = min(top.
	 *  height, cur.height) - poped.height. we also need distance = cur - top -  1.
	 *  ans += bound * distance.
	 *  
	 * T: O(n)
	 * S: O(n)
	 * */
	public int trapIII(int[] height) {
        int ans = 0, cur = 0;
        Deque<Integer> stack = new LinkedList<>();
        int len = height.length;
        while (cur < len) {
            while (!stack.isEmpty() && height[cur] > height[stack.peekFirst()]) {
                int top = stack.pollFirst();
                if (stack.isEmpty()) break;
                int distance = cur - stack.peekFirst() - 1;
                int bound = Math.min(height[stack.peekFirst()], height[cur]) - height[top];
                ans += bound * distance;
            }
            stack.offerFirst(cur++);
        }
        return ans;
    }
	
	/*
	 * Algorithm 4: two pointers
	 *   We do one iteration from two directions
	 *   since it always depends on the lowest bar between left and right. 
	 *   if (height[left] < height[right])  we can go from left to right.
	 *   update leftMax if height[left] > leftMax, else it can store water:
	 *   leftMax -  height[left]. and the right part vice versa.
	 *   
	 * T: O(n)
	 * S: O(1)
	 * */
	public int trapIV(int[] height) {
        int ans = 0;
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    ans += (leftMax - height[left]);
                }
                ++left;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    ans += (rightMax - height[right]);
                }
                --right;
            }
        }
        return ans;
    }
}
