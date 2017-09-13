using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KursovaLibrary
{
    public class ClusterTree
    {
        public int level;
        public int numberOfLeaf;
        public int[] leaf;
        public ClusterTree leftTree;
        public ClusterTree rightTree;

        public static int getNumberOfNodes(int p)
        {
            if (p == 1)
            {
                return 3;
            }
            else
            {
                return 2 * getNumberOfNodes(p - 1) + 1;
            }
        }
        public static ClusterTree buildClusterTree(int n)
        {
            int p = (int)Math.Log(n, 2);
            int[] arr = new int[p + 1];
            for (int i = 0; i <= p; i++)
            {
                arr[i] = (int)Math.Pow(2, i);
            }
            ClusterTree t = buildClusterTreeRec(0, n - 1, 0, 0, arr);
            return t;
        }
        public static ClusterTree buildClusterTreeRec(int b, int e, int level, int num, int[] arr)
        {
            ClusterTree t = new ClusterTree();
            t.level = level;
            t.numberOfLeaf = num;
            int n = e - b + 1;
            t.leaf = new int[n];
            for (int i = 0; i < t.leaf.Length; i++)
            {
                t.leaf[i] = i + b;
            }
            int m = (int)Math.Pow(2, level) - arr[level];
            arr[level]--;
            if (n != 1)
            {
                t.leftTree = buildClusterTreeRec(b, (b + e + 1) / 2 - 1, level + 1, m, arr);
                m = (int)Math.Pow(2, level) - arr[level];
                arr[level]--;
                t.rightTree = buildClusterTreeRec((b + e + 1) / 2, e, level + 1, m, arr);
            }
            else
            {
                t.leftTree = null;
                t.rightTree = null;
            }
            return t;
        }
        public static ClusterTree getByIndex(ClusterTree t, int l, int n)
        {
            if (t == null)
            {
                return null;
            }
            else
            {
                if ((t.level) == l && (t.numberOfLeaf == n))
                {
                    return t;
                }
                else
                {
                    ClusterTree temp = getByIndex(t.rightTree, l, n);
                    if (temp == null)
                    {
                        ClusterTree temp2 = getByIndex(t.leftTree, l, n);
                        if (temp2 == null)
                        {
                            return null;
                        }
                        else
                        {
                            return temp2;
                        }
                    }
                    else
                    {
                        return temp;
                    }
                }
            }
        }
    }
}
